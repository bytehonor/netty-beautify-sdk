package com.bytehonor.sdk.beautify.netty.common.core;

import com.bytehonor.sdk.beautify.netty.common.model.NettyPayload;

/**
 * @author lijianqiang
 *
 */
public interface NettyConsumer {

    public String subject();

    public void consume(String stamp, NettyPayload payload);
}
