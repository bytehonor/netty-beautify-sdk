package com.bytehonor.sdk.netty.bytehonor.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultServerListener implements ServerListener {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultServerListener.class);

    @Override
    public void onSucceed() {
        LOG.info("Server onSucceed");

    }

    @Override
    public void onFailed(Throwable error) {
        LOG.error("Server onFailed", error);

    }

    @Override
    public void onTotal(int total) {
        LOG.info("Server onTotal:{}", total);
    }
}
