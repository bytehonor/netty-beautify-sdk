package com.bytehonor.sdk.netty.bytehonor.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.netty.bytehonor.common.handler.NettyMessageReceiver;
import com.bytehonor.sdk.netty.bytehonor.common.handler.NettyMessageSender;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lijianqiang
 *
 */
public class NettyClientByteHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClientByteHandler.class);

    private String whois;

    public NettyClientByteHandler() {
        this(null);
    }

    public NettyClientByteHandler(String whois) {
        this.whois = whois;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 客户端上传消息
        Channel channel = ctx.channel();
        if (msg instanceof ByteBuf) {
            NettyMessageReceiver.receiveByteBuf(channel, (ByteBuf) msg);
        } else {
            String remoteAddress = channel.remoteAddress().toString();
            LOG.error("channelRead unknown msg:{}, remoteAddress:{}, channelId:{}", msg.toString(), remoteAddress,
                    channel.id().asLongText());
        }
    }

    // 当连接建立好的使用调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (this.whois != null) {
            NettyMessageSender.whoisClient(channel, this.whois);
        }
        String remoteAddress = channel.remoteAddress().toString();
        LOG.info("channelActive whois:{}, remoteAddress:{}, channelId:{}", whois, remoteAddress,
                channel.id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        String remoteAddress = channel.remoteAddress().toString();
        LOG.error("exceptionCaught remoteAddress:{}, channelId:{}, error", remoteAddress, channel.id().asLongText(),
                cause);
        ctx.close();
    }
}
