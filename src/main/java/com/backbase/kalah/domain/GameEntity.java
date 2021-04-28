package com.backbase.kalah.domain;

import com.backbase.kalah.engine.Pit;
import com.backbase.kalah.engine.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("Game")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

    @Id
    private Long id;
    private Pit[] pits;
    private Player currentPlayer;
    private boolean ended;
}

