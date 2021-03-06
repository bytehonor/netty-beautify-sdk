package com.bytehonor.sdk.beautify.netty.common.listener;

import io.netty.channel.Channel;

public interface NettyClientListener {

    public void onOpen(Channel channel);

    public void onClosed(String msg);

    public void onError(Throwable error);
}
