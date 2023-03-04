package com.bytehonor.sdk.beautify.netty.common.model;

import java.io.Serializable;

import com.bytehonor.sdk.beautify.netty.common.constant.NettyConstants;

/**
 * @author lijianqiang
 *
 */
public class NettyServerConfig implements Serializable {

    private static final long serialVersionUID = 1158507238071086040L;

    private int port;

    private boolean ssl;

    private boolean sslEngine;

    private String sslPassword;

    // idle
    private int readIdleSeconds;
    private int writIdleSeconds;
    private int allIdleSeconds;

    // length
//    private int maxFrameLength;
//    private int lengthFieldOffset;
//    private int lengthFieldLength;

    // Threads
    private int bossThreads;
    private int workThreads;
//    private int clientThreads;
//
//    private int connectTimeoutMills;

    /**
     * 服务端检查任务周期
     */
    private long periodSeconds;

    public NettyServerConfig() {
        this.port = 85;
        this.ssl = false;
        this.sslEngine = false;
        this.sslPassword = NettyConstants.SSL_PASSWORD;
        this.readIdleSeconds = NettyConstants.READ_IDLE_TIMEOUT_SECONDS;
        this.writIdleSeconds = NettyConstants.WRITE_IDLE_TIMEOUT_SECONDS;
        this.allIdleSeconds = NettyConstants.ALL_IDLE_TIMEOUT_SECONDS;
//        this.maxFrameLength = NettyConstants.MAX_LENGTH;
//        this.lengthFieldOffset = NettyConstants.LENGTH_OFFSET;
//        this.lengthFieldLength = NettyConstants.LENGTH_SIZE;
        this.bossThreads = NettyConstants.BOSS_THREADS;
        this.workThreads = NettyConstants.WORD_THREADS;
//        this.clientThreads = NettyConstants.CLIENT_THREADS;
//        this.connectTimeoutMills = NettyConstants.CONNECT_TIMEOUT_MILLIS;
        this.periodSeconds = 100L;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isSslEngine() {
        return sslEngine;
    }

    public void setSslEngine(boolean sslEngine) {
        this.sslEngine = sslEngine;
    }

    public String getSslPassword() {
        return sslPassword;
    }

    public void setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
    }

    public int getReadIdleSeconds() {
        return readIdleSeconds;
    }

    public void setReadIdleSeconds(int readIdleSeconds) {
        this.readIdleSeconds = readIdleSeconds;
    }

    public int getWritIdleSeconds() {
        return writIdleSeconds;
    }

    public void setWritIdleSeconds(int writIdleSeconds) {
        this.writIdleSeconds = writIdleSeconds;
    }

    public int getAllIdleSeconds() {
        return allIdleSeconds;
    }

    public void setAllIdleSeconds(int allIdleSeconds) {
        this.allIdleSeconds = allIdleSeconds;
    }

    public int getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public int getWorkThreads() {
        return workThreads;
    }

    public void setWorkThreads(int workThreads) {
        this.workThreads = workThreads;
    }

    public long getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(long periodSeconds) {
        this.periodSeconds = periodSeconds;
    }

}