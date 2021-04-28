package com.backbase.kalah.service.mapper;

import com.backbase.kalah.domain.GameEntity;
import com.backbase.kalah.engine.Game;
import com.backbase.kalah.service.GameIdService;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    private final GameIdService gameIdService;

    public GameMapper(GameIdService gameIdService) {
        this.gameIdService = gameIdService;
    }

    public GameEntity map(Game game) {
        return GameEntity.builder()
                .id(game.getId() == null ? gameIdService.getLastGameId().getValue() : game.getId())
                .currentPlayer(game.getCurrentPlayer())
                .ended(game.isEnded())
                .pits(game.getPits())
                .build();
    }

    public Game map(GameEntity gameEntity, int pitCount, int stoneCount) {
        Game game = new Game(pitCount, stoneCount);
        game.setId(gameEntity.getId());
        game.setCurrentPlayer(gameEntity.getCurrentPlayer());
        game.setEnded(gameEntity.isEnded());
        game.setPits(gameEntity.getPits());
        return game;
    }
}

