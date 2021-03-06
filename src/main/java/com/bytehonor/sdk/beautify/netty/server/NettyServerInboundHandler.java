package com.bytehonor.sdk.beautify.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.beautify.netty.common.cache.ChannelCacheManager;
import com.bytehonor.sdk.beautify.netty.common.handler.NettyMessageReceiver;
import com.bytehonor.sdk.beautify.netty.common.handler.NettyMessageSender;
import com.bytehonor.sdk.beautify.netty.common.listener.NettyServerListener;
import com.bytehonor.sdk.beautify.netty.common.listener.ServerListenerHelper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lijianqiang
 *
 */
public class NettyServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServerInboundHandler.class);

    private final String whoiam;

    private final NettyServerListener listener;

    public NettyServerInboundHandler(String whoiam, NettyServerListener listener) {
        this.whoiam = whoiam != null ? whoiam : "unknown";
        this.listener = listener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 客户端上传消息
        Channel channel = ctx.channel();
        if (msg instanceof ByteBuf) {
            NettyMessageReceiver.receiveByteBuf(channel, (ByteBuf) msg);
            return;
        }
        LOG.error("channelRead unknown msg:{}, remoteAddress:{}, channelId:{}", msg.getClass().getSimpleName(),
                channel.remoteAddress().toString(), channel.id().asLongText());

    }

    /**
     * 
     * @param ctx   当前的应用上下文
     * @param cause Throwable是异常和Error的顶级接口,此处就是异常
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        onRemoved(channel);
        LOG.error("exceptionCaught remoteAddress:{}, channelId:{}, error", channel.remoteAddress().toString(),
                channel.id().asLongText(), cause);
        ctx.close();
    }

    /**
     * 当客户连接服务端之后（打开链接） 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        onAdded(channel);
        LOG.info("handlerAdded remoteAddress:{}, channelId:{}", channel.remoteAddress().toString(),
                channel.id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        onRemoved(channel);
        String remoteAddress = channel.remoteAddress().toString();
        LOG.info("handlerRemoved remoteAddress:{}, channelId:{}", remoteAddress, channel.id().asLongText());
    }

    private void onRemoved(Channel channel) {
        ChannelCacheManager.remove(channel);
        ServerListenerHelper.onTotal(listener, ChannelCacheManager.size());
    }

    private void onAdded(Channel channel) {
        ChannelCacheManager.add(channel);
        NettyMessageSender.whoisServer(channel, whoiam);
        ServerListenerHelper.onTotal(listener, ChannelCacheManager.size());
    }
}
