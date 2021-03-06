package com.bytehonor.sdk.beautify.netty.server;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.beautify.netty.common.handler.SubjectHandler;
import com.bytehonor.sdk.beautify.netty.common.handler.SubjectHandlerFactory;
import com.bytehonor.sdk.beautify.netty.common.limiter.SubjectLimiter;
import com.bytehonor.sdk.beautify.netty.common.limiter.SubjectSubscribeLimiter;
import com.bytehonor.sdk.beautify.netty.common.listener.DefaultNettyServerListener;
import com.bytehonor.sdk.beautify.netty.common.listener.NettyServerListener;
import com.bytehonor.sdk.beautify.netty.common.model.NettyConfig;
import com.bytehonor.sdk.beautify.netty.common.model.NettyConfigBuilder;
import com.bytehonor.sdk.beautify.netty.common.task.NettyScheduleTaskExecutor;
import com.bytehonor.sdk.beautify.netty.common.task.NettyTaskBuilder;

public class NettyServerContanier {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServerContanier.class);

    private final NettyServer server;

    private NettyServerContanier() {
        this.server = new NettyServer();
    }

    /**
     * 延迟加载(线程安全)
     *
     */
    private static class LazyHolder {
        private static NettyServerContanier SINGLE = new NettyServerContanier();
    }

    private static NettyServerContanier self() {
        return LazyHolder.SINGLE;
    }

    public static void start(int port) {
        start(port, new DefaultNettyServerListener());
    }

    public static void start(int port, NettyServerListener listener) {
        start(NettyConfigBuilder.server(port).build(), listener);
    }

    public static void start(NettyConfig config) {
        start(config, new DefaultNettyServerListener());
    }

    public static void start(NettyConfig config, NettyServerListener listener) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(listener, "listener");
        LOG.info("start...");

        self().server.start(config, listener);

        NettyScheduleTaskExecutor.scheduleAtFixedRate(NettyTaskBuilder.serverCheck(), 20L, config.getPeriodSeconds());
    }

    public static void addHandler(SubjectHandler handler) {
        Objects.requireNonNull(handler, "handler");
        Objects.requireNonNull(handler.subject(), "subject");
        SubjectHandlerFactory.put(handler);
    }

    public static void addLimiter(String subject, int limit) {
        Objects.requireNonNull(subject, "subject");
        SubjectSubscribeLimiter.add(SubjectLimiter.of(subject, limit));
    }

    public static void limit() {
        SubjectSubscribeLimiter.limits();
    }
}
