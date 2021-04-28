package com.backbase.kalah.service;


import com.backbase.kalah.domain.GameIdEntity;
import com.backbase.kalah.reposiroty.GameIdRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameIdService {

    private static final String ID = "GAME_ID";
    private final GameIdRepository gameIdRepository;

    public GameIdService(GameIdRepository gameIdRepository) {
        this.gameIdRepository = gameIdRepository;
    }

    public GameIdEntity getLastGameId() {
        Optional<GameIdEntity> findById = gameIdRepository.findById(ID);
        if (!findById.isPresent()) {
            return gameIdRepository.save(GameIdEntity.builder().id(ID).value(1L).build());
        }
        findById.get().setValue(findById.get().getValue() + 1);
        gameIdRepository.save(findById.get());
        return findById.get();
    }
}
