package com.bytehonor.sdk.beautify.netty.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.beautify.netty.common.constant.NettyTypeEnum;
import com.bytehonor.sdk.beautify.netty.common.model.NettyPayload;
import com.bytehonor.sdk.beautify.netty.common.task.NettyPayloadTask;

import io.netty.channel.Channel;

public class NettyPublicPayloadHandler implements NettyHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NettyPublicPayloadHandler.class);

    @Override
    public int type() {
        return NettyTypeEnum.PUBLIC_PAYLOAD.getType();
    }

    @Override
    public void handle(Channel channel, String message) {
        NettyPayload payload = NettyPayload.fromJson(message);
        if (LOG.isDebugEnabled()) {
            LOG.debug("subject:{}, body:{}", payload.getSubject(), payload.getBody());
        }

        // 单线程处理
        NettyPayloadTask.add(payload);
    }

}
