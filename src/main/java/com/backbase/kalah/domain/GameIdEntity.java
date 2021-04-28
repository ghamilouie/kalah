package com.backbase.kalah.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("GameId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameIdEntity {
    private String id;
    @Getter
    @Setter
    private Long value;
}

