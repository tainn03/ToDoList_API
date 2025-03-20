package com.tainn.todo.infrastructure.config.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FastJsonRedisDataSerializer<T> implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Class<T> clazz;

    public FastJsonRedisDataSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (null == t) {
            return new byte[0];
        }
        return JSON.toJSONString(t,
                        SerializerFeature.WriteClassName,
                        SerializerFeature.DisableCircularReferenceDetect)
                .getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz);
    }
}
