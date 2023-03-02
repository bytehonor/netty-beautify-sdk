package com.bytehonor.sdk.beautify.netty.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.beautify.netty.common.cache.ChannelCacheHolder;
import com.bytehonor.sdk.beautify.netty.common.cache.StampChannelHolder;

/**
 * @author lijianqiang
 *
 */
public class NettyServerCheckTask extends NettyTask {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServerCheckTask.class);

    @Override
    public void runInSafe() {
        LOG.info("channel size:{}, stamp size:{}", ChannelCacheHolder.size(), StampChannelHolder.size());

//        Iterator<Entry<String, ChannelIdCacheHolder>> its = SubjectChannelCacheHolder.entrySet().iterator();
//        while (its.hasNext()) {
//            Entry<String, ChannelIdCacheHolder> item = its.next();
//            String subject = item.getKey();
//            ChannelIdCacheHolder holder = item.getValue();
//            LOG.info("subject:{}, channels size:{}", subject, holder.size());
//            if (holder.size() > 0) {
//                Set<ChannelId> channels = holder.values();
//                for (ChannelId channel : channels) {
//                    if (ChannelCacheManager.exists(channel)) {
//                        continue;
//                    }
//                    LOG.warn("remove subject:{}, channel:{}", subject, channel.asLongText());
//                    holder.remove(channel);
//                }
//            }
//        }

        // 限幅
//        NettyServerContanier.limit();
    }

}
