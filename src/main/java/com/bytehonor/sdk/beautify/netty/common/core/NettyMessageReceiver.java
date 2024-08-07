package com.bytehonor.sdk.beautify.netty.common.core;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.beautify.netty.common.consumer.NettyConsumer;
import com.bytehonor.sdk.beautify.netty.common.consumer.NettyConsumerFactory;
import com.bytehonor.sdk.beautify.netty.common.consumer.NettyConsumerProcess;
import com.bytehonor.sdk.beautify.netty.common.model.NettyFrame;
import com.bytehonor.sdk.beautify.netty.common.model.NettyMessage;
import com.bytehonor.sdk.beautify.netty.common.task.NettyMessageTask;

/**
 * 放入线程池处理
 * 
 * @author lijianqiang
 *
 */
public class NettyMessageReceiver implements NettyMessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NettyMessageReceiver.class);

    private final NettyConsumerFactory factory;

    public NettyMessageReceiver() {
        this.factory = new NettyConsumerFactory();
    }

    public final void addConsumer(NettyConsumer consumer) {
        Objects.requireNonNull(consumer, "consumer");
        this.factory.add(consumer);
    }

    public final void addMessage(NettyMessage message) {
        if (message == null) {
            LOG.warn("message null");
            return;
        }
        NettyMessagePoolExecutor.add(NettyMessageTask.of(message, this));
    }

    @Override
    public final void handle(NettyMessage message) {
        String stamp = message.getStamp();
        NettyFrame frame = NettyFrame.fromJson(message.getFrame());
        if (LOG.isDebugEnabled()) {
            LOG.debug("process method:{}, subject:{}, stamp:{}", frame.getMethod(), frame.getSubject(), stamp);
        }

        final String method = frame.getMethod();
        if (method == null) {
            LOG.error("method null, message:{}", message.getFrame());
            return;
        }

        switch (method) {
        case NettyFrame.PING:
            NettyMessageSender.pong(stamp);
            break;
        case NettyFrame.PONG:
            doPong(stamp);
            break;
        case NettyFrame.PAYLOAD:
            NettyConsumerProcess.process(stamp, frame, factory.get(frame.getSubject()));
            break;
        default:
            LOG.warn("unkonwn method:{}", method);
            break;
        }
    }

    private static void doPong(String stamp) {
        LOG.debug("stamp:{}", stamp);
    }

}
