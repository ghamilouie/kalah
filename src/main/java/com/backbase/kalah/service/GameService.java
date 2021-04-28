package com.backbase.kalah.service;

import com.backbase.kalah.domain.GameEntity;
import com.backbase.kalah.dto.GameResponse;
import com.backbase.kalah.dto.GameStatusResponse;
import com.backbase.kalah.dto.RestResponse;
import com.backbase.kalah.engine.Game;
import com.backbase.kalah.exception.ErrorDefinition;
import com.backbase.kalah.exception.GameException;
import com.backbase.kalah.reposiroty.GameRepository;
import com.backbase.kalah.service.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    @Value("${game.pit.count}")
    private int pitCount;
    @Value("${game.stone.count}")
    private int stoneCount;

    @Autowired
    public GameService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public GameResponse createGame() {
        Game game = new Game(pitCount, stoneCount);
        GameEntity save = gameRepository.save(gameMapper.map(game));
        return GameResponse.builder()
                .id(save.getId())
                .build();
    }

    public GameStatusResponse move(Long gameId, int pitId) {
        Optional<GameEntity> findById = gameRepository.findById(gameId);
        if (!findById.isPresent()) {
            throw new GameException(ErrorDefinition.GAME_NOT_FOUND);
        }
        GameEntity gameEntity = findById.get();
        Game game = gameMapper.map(gameEntity, pitCount, stoneCount);
        game.move(pitId);
        gameRepository.save(gameMapper.map(game));
        return GameStatusResponse.builder()
                .id(gameId)
                .status(game.getStatusMap())
                .build();
    }
}
