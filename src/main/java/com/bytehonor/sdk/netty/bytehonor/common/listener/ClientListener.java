package com.bytehonor.sdk.netty.bytehonor.common.listener;

import io.netty.channel.Channel;

public interface ClientListener {

    public void onOpen(Channel channel);

    public void onClosed(String msg);

    public void onError(Throwable error);
}