package com.bytehonor.sdk.beautify.netty.common.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijianqiang
 *
 */
public class NettyHandlerFactory {

    private static final Map<Integer, NettyHandler> MAP = new ConcurrentHashMap<Integer, NettyHandler>();

    static {
        put(new NettyPingHandler());
        put(new NettyPongHandler());
        put(new NettySubscribeRequestHandler());
        put(new NettySubscribeResponseHandler());
        put(new NettyWhoisClientHandler());
        put(new NettyWhoisServerHandler());
        put(new NettyPayloadHandler());
    }

    public static void put(NettyHandler handler) {
        if (handler == null) {
            return;
        }
        MAP.put(handler.type(), handler);
    }

    public static NettyHandler get(int type) {
        return MAP.get(type);
    }
}
