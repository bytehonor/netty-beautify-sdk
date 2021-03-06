package com.bytehonor.sdk.beautify.netty.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.beautify.netty.common.util.NettyJsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 20220124
 * 
 * @author lijianqiang
 *
 */
public class NettyPayloads implements Serializable {

    private static final long serialVersionUID = 2699241336868045496L;

    /**
     * Body Object的Clazz全名就是subject
     */
    private String subject;

    private String body;

    public static NettyPayloads fromJson(String json) {
        Objects.requireNonNull(json, "json");
        return NettyJsonUtils.fromJson(json, NettyPayloads.class);
    }

    public static <T> NettyPayloads fromOne(T obj) {
        Objects.requireNonNull(obj, "obj");

        ArrayList<T> list = new ArrayList<T>();
        list.add(obj);
        return fromList(list);
    }

    public static <T> NettyPayloads fromList(List<T> list) {
        Objects.requireNonNull(list, "list");
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("list cannot be empty");
        }

        NettyPayloads model = new NettyPayloads();
        model.setSubject(list.get(0).getClass().getName());
        model.setBody(NettyJsonUtils.toJson(list));
        return model;
    }

    public <T> List<T> list(TypeReference<List<T>> valueTypeRef) {
        Objects.requireNonNull(valueTypeRef, "valueTypeRef");
        try {
            return NettyJsonUtils.fromJson(this.body, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> list(Class<T> valueType) {
        Objects.requireNonNull(valueType, "valueType");
        if (valueType.getName().equals(this.subject) == false) {
            throw new RuntimeException(this.subject);
        }
        try {
            return NettyJsonUtils.fromListJson(this.body, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T one(Class<T> valueType) {
        Objects.requireNonNull(valueType, "valueType");
        List<T> list = this.list(valueType);
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException(this.subject);
        }
        return list.get(0);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return NettyJsonUtils.toJson(this);
    }

}
