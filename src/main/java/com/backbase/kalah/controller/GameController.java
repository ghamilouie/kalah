package com.backbase.kalah.controller;

import com.backbase.kalah.dto.GameResponse;
import com.backbase.kalah.dto.RestResponse;
import com.backbase.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<? extends RestResponse> createGame(HttpServletRequest request) {
        GameResponse game = gameService.createGame();
        return ResponseEntity.ok(addInfo(game, request, game.getId()));
    }

    @PutMapping("{gameId}/pits/{pitId}")
    public ResponseEntity<? extends RestResponse> move(HttpServletRequest request, @PathVariable Long gameId, @PathVariable int pitId) {
        return ResponseEntity.ok(addInfo(gameService.move(gameId, pitId), request, gameId));
    }

    private RestResponse addInfo(RestResponse response, HttpServletRequest request, Long gameId) {
        response.setUri("http://" + request.getLocalAddr() + ":" + request.getLocalPort() + "/games/" + gameId);
        return response;
    }
}
