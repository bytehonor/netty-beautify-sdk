package com.bytehonor.sdk.beautify.netty.client;

import com.bytehonor.sdk.beautify.netty.common.core.NettyIdleStateChecker;
import com.bytehonor.sdk.beautify.netty.common.core.NettyLengthFrameDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lijianqiang
 *
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private final String stamp;

    private final NettyClientHandler handler;

    public NettyClientInitializer(String stamp, NettyClientHandler handler) {
        this.stamp = stamp;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 自定义的空闲检测
//        pipeline.addLast(new IdleStateHandler(config.getReadIdleSeconds(), config.getWritIdleSeconds(),
//                config.getAllIdleSeconds()));
        pipeline.addLast(new NettyIdleStateChecker());

        // byte数组
        pipeline.addLast(new NettyLengthFrameDecoder());
        pipeline.addLast(new NettyClientInboundHandler(stamp, handler));

        // 字符串
        // ByteBuf buf =
        // Unpooled.copiedBuffer(ProtocolConstants.END.getBytes(CharsetUtil.UTF_8));
        // pipeline.addLast(new DelimiterBasedFrameDecoder(2 * 1024, buf));
        // pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        // pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new NettyClientHeartBeatHandler());

    }
}
