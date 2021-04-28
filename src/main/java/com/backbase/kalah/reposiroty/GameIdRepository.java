package com.backbase.kalah.reposiroty;


import com.backbase.kalah.domain.GameIdEntity;
import org.springframework.data.repository.CrudRepository;

public interface GameIdRepository extends CrudRepository<GameIdEntity, String> {
}
