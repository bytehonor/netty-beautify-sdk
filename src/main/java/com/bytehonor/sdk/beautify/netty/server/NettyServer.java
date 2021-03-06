package com.bytehonor.sdk.beautify.netty.server;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.beautify.netty.common.constant.NettyConstants;
import com.bytehonor.sdk.beautify.netty.common.listener.DefaultNettyServerListener;
import com.bytehonor.sdk.beautify.netty.common.listener.NettyServerListener;
import com.bytehonor.sdk.beautify.netty.common.listener.ServerListenerHelper;
import com.bytehonor.sdk.beautify.netty.common.model.NettyConfig;
import com.bytehonor.sdk.beautify.netty.common.model.NettyConfigBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lijianqiang
 *
 */
public class NettyServer {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    /**
     * 负责连接请求
     */
    private EventLoopGroup bossGroup;

    /**
     * 负责事件响应
     */
    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    private ChannelFuture channelFuture;

    private boolean init = false;

    public NettyServer() {
        init = false;
    }

    public void start(int port) {
        NettyConfig config = NettyConfigBuilder.server(port).build();
        start(config, new DefaultNettyServerListener());
    }

    public void start(NettyConfig config, NettyServerListener listener) {
        bind(config, listener);
    }

    private void bind(NettyConfig config, NettyServerListener listener) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(listener, "listener");

        if (init) {
            return;
        }

        LOG.info("Netty server start, port:{}, ssl:{}", config.getPort(), config.isSsl());
        init = true;

        // 负责连接请求
        bossGroup = new NioEventLoopGroup(config.getBossThreads());
        // 负责事件响应
        workerGroup = new NioEventLoopGroup(config.getWorkThreads());
        // 负责连接请求
        // EventLoopGroup bossGroup = new NioEventLoopGroup(4);
        // 负责事件响应
        // EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            // 服务器启动项
            bootstrap = new ServerBootstrap();
            // handler是针对bossGroup，childHandler是针对workerHandler
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.option(ChannelOption.SO_BACKLOG, NettyConstants.SO_BACKLOG); // 设置TCP缓冲区
            bootstrap.option(ChannelOption.SO_RCVBUF, NettyConstants.SO_RCVBUF); // 接收客户端信息的最大长度
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 选择nioChannel
            bootstrap.channel(NioServerSocketChannel.class);
            // 日志处理 info级别
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            // 添加自定义的初始化器
            bootstrap.childHandler(new NettyServerInitializer(config, listener));

            // 端口绑定
            channelFuture = bootstrap.bind(config.getPort());
            LOG.info("Netty Tcp start isSuccess:{}", channelFuture.isSuccess());
            // channelFuture = bootstrap.bind(port).sync();
            // 该方法进行阻塞,等待服务端链路关闭之后继续执行。
            // 这种模式一般都是使用Netty模块主动向服务端发送请求，然后最后结束才使用
            // channelFuture.channel().closeFuture().sync();
            ServerListenerHelper.onSucceed(listener);
        } catch (Exception e) {
            LOG.error("Netty server start error", e);
            ServerListenerHelper.onFailed(listener, e);
        }
    }

}
