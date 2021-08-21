package com.bytehonor.sdk.netty.bytehonor.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.netty.bytehonor.common.handler.NettyMessageSender;
import com.bytehonor.sdk.netty.bytehonor.common.model.SubscribeRequest;

public final class NettyClientContanier {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClientContanier.class);

    private NettyClient client;

    private String host;

    private int port;

    private static boolean ping = false;

    private static final Map<String, SubscribeRequest> MAP = new HashMap<String, SubscribeRequest>();

    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();

    private NettyClientContanier() {
    }

    /**
     * 延迟加载(线程安全)
     *
     */
    private static class LazyHolder {
        private static NettyClientContanier instance = new NettyClientContanier();
    }

    private static NettyClientContanier getInstance() {
        return LazyHolder.instance;
    }

    public static void connect(String host, int port) {
        LOG.info("connect begin ...");
        getInstance().host = host;
        getInstance().port = port;
        if (getInstance().client != null) {
            getInstance().client.getChannel().close();
        }
        getInstance().client = new NettyClient(host, port);
        getInstance().client.start();
        ping();
    }

    private static void ping() {
        if (ping == false) {
            LOG.info("ping begin ...");
            ping = true;
            // 连接成功后，设置定时器，每隔25，自动向服务器发送心跳，保持与服务器连接
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // task to run goes here
                    try {
                        NettyMessageSender.ping(getInstance().client.getChannel());
                    } catch (Exception e) {
                        LOG.error("ping error:{}", e.getMessage());
                        reconnect();
                    }
                }
            };
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            SERVICE.scheduleAtFixedRate(runnable, 20, 35, TimeUnit.SECONDS);
        }
    }

    public static void reconnect() {
        LOG.info("reconnect begin ...");
        connect(getInstance().host, getInstance().port);
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                LOG.error("sleep", e);
            }
            if (isConnected()) {
                LOG.info("reconnect connected ok");
                break;
            }
        }
        if (isConnected() == false) {
            LOG.error("reconnect connected failed");
            return;
        }
        // 把任务重新订阅
        if (MAP.isEmpty() == false) {
            LOG.info("subscribe again ...");
            for (Entry<String, SubscribeRequest> item : MAP.entrySet()) {
                subscribe(item.getValue());
            }
        }
    }

    public static boolean isConnected() {
        return getInstance().client.getChannel().isActive();
    }

    public static void send(String value) {
        NettyMessageSender.send(getInstance().client.getChannel(), value);
    }

    public static void subscribe(SubscribeRequest request) {
        if (request == null) {
            return;
        }
        if (MAP.get(request.getCategory()) == null) {
            MAP.put(request.getCategory(), request);
        }
        NettyMessageSender.subscribeRequest(getInstance().client.getChannel(), request);
    }
}
