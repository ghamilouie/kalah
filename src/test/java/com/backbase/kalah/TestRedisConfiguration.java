package com.backbase.kalah;


import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisConfiguration {

    private static final int port = 6979;
    private static RedisServer redisServer;

    @PostConstruct
    protected void before() {
        if (redisServer == null) {
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("bind 127.0.0.1")
                    .setting("maxmemory 128M")
                    .build();
            redisServer.start();
        }
    }

    @PreDestroy
    protected void after() {
        if (redisServer != null) {
            redisServer.stop();
            redisServer = null;
        }
    }

}
