package com.bytehonor.sdk.beautify.netty.common.handler;

import com.bytehonor.sdk.beautify.netty.common.consumer.NettyConsumerFactory;
import com.bytehonor.sdk.beautify.netty.common.model.NettyFrame;
import com.bytehonor.sdk.beautify.netty.common.model.NettyPayload;

/**
 * @author lijianqiang
 *
 */
public class NettySubscribeResponseHandler implements NettyFrameHandler {

//    private static final Logger LOG = LoggerFactory.getLogger(NettySubscribeResponseHandler.class);

    @Override
    public String method() {
        return NettyFrame.SUBSCRIBE;
    }

    @Override
    public void handle(String stamp, NettyPayload payload, NettyConsumerFactory factory) {
//        SubscribeResponse response = NettyPayload.reflect(message, SubscribeResponse.class);
//        LOG.info("completed:{}, subject:{}", response.getCompleted(), response.getSubject());
    }

}