package com.bytehonor.sdk.beautify.netty.common.core;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.bytehonor.sdk.beautify.netty.common.task.NettyTask;

/**
 * 多线程
 */
public class NettyMessagePoolExecutor {

    private static final String NAMED = "netty-message-thread-";

    private final ExecutorService executor;

    private NettyMessagePoolExecutor() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(nThreads, new CustomizableThreadFactory(NAMED));
    }

    private static class LazyHolder {
        private static NettyMessagePoolExecutor SINGLE = new NettyMessagePoolExecutor();
    }

    private static NettyMessagePoolExecutor self() {
        return LazyHolder.SINGLE;
    }

    private void execute(NettyTask task) {
        executor.execute(task);
    }

    /**
     * 
     * @param task
     */
    public static void add(NettyTask task) {
        Objects.requireNonNull(task, "task");

        self().execute(task);
    }
}
