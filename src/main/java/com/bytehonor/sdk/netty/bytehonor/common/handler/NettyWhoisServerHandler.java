package com.bytehonor.sdk.netty.bytehonor.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.netty.bytehonor.common.WhoisServerCacheHolder;
import com.bytehonor.sdk.netty.bytehonor.common.constant.NettyTypeEnum;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

/**
 * @author lijianqiang
 *
 */
public class NettyWhoisServerHandler implements NettyHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NettyWhoisServerHandler.class);

    @Override
    public int type() {
        return NettyTypeEnum.WHOIS_SERVER.getType();
    }

    @Override
    public void handle(Channel channel, String message) {
        ChannelId channelId = channel.id();
        LOG.info("whois:{}, channelId:{}", message, channelId.asLongText());
        WhoisServerCacheHolder.put(message, channelId);
    }

}