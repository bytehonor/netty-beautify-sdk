package com.bytehonor.sdk.netty.bytehonor.common.handler;

import io.netty.channel.Channel;

/**
 * @author lijianqiang
 *
 */
public interface NettyHandler {

    public int type();

    public void handle(Channel channel, String message);
}
