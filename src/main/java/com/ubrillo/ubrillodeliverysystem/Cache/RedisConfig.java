package com.ubrillo.ubrillodeliverysystem.Cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

/**
 * Configuration class for Redis cache support.
 * <p>
 * This class defines the Redis template bean used to serialize and deserialize
 * {@link OrderState} objects when storing them in Redis.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a Redis template for working with order state data.
     * <p>
     * The template uses string serialization for Redis keys and JSON
     * serialization for Redis values.
     *
     * @param connectionFactory Redis connection factory provided by Spring.
     * @return Configured {@link RedisTemplate} for {@link OrderState} values.
     */
    @Bean
    public RedisTemplate<String, OrderState> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, OrderState> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        // Serializer used for Redis keys and hash keys.
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Serializer used for Redis values and hash values.
        GenericJacksonJsonRedisSerializer jsonSerializer = new GenericJacksonJsonRedisSerializer(new ObjectMapper());

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();

        return template;
    }
}
