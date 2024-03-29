package com.bytehonor.sdk.beautify.netty.common.limiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.beautify.netty.common.cache.SubjectChannelCacheHolder;

import io.netty.channel.ChannelId;

public class NettySubjectLimiter {

    private static final Logger LOG = LoggerFactory.getLogger(NettySubjectLimiter.class);

    private static final List<SubjectLimiter> LIST = new ArrayList<SubjectLimiter>();

    public static void add(SubjectLimiter limiter) {
        Objects.requireNonNull(limiter, "limiter");
        Objects.requireNonNull(limiter.getSubject(), "subject");
        LIST.add(limiter);
    }

    public static void limits() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("LIST size:{}", LIST.size());
        }
        for (SubjectLimiter limiter : LIST) {
            doLimit(limiter.getSubject(), limiter.getLimit());
        }
    }

    public static void limit(String subject, int limit) {
        doLimit(subject, limit);
    }

    public static void limit(SubjectLimiter limiter) {
        Objects.requireNonNull(limiter, "limiter");

        doLimit(limiter.getSubject(), limiter.getLimit());
    }

    private static void doLimit(String subject, int limit) {
        Objects.requireNonNull(subject, "subject");

        Set<ChannelId> raw = SubjectChannelCacheHolder.list(subject);
        if (CollectionUtils.isEmpty(raw)) {
            return;
        }
        List<ChannelId> channels = new ArrayList<ChannelId>(raw);
        int size = channels.size();
        int remove = size - limit;
        if (remove < 1) {
            return;
        }
        LOG.warn("subject:{}, remove:{}, limit:{}, size:{} overlimit.", subject, remove, limit, size);

        List<ChannelId> targets = new ArrayList<ChannelId>();
        for (int i = 0; i < remove; i++) {
            targets.add(channels.get(i));
        }
//        for (ChannelId target : targets) {
//            try {
//                Channel last = ChannelCacheManager.getChannel(target);
//                if (last != null) {
//                    last.close();
//                }
//                SubjectChannelCacheHolder.remove(subject, target);
//            } catch (Exception e) {
//                LOG.error("error", e);
//            }
//        }
    }
}
