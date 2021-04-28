package com.backbase.kalah.game;

import com.backbase.kalah.engine.Game;
import com.backbase.kalah.engine.Pit;
import com.backbase.kalah.engine.Player;
import com.backbase.kalah.exception.ErrorDefinition;
import com.backbase.kalah.exception.GameException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameTest {

    private Game getGame() {
        return new Game(14, 6);
    }

    @Test
    public void createGame() {
        Game game = getGame();
        Pit[] pits = game.getPits();
        int sum = Arrays.stream(pits).mapToInt(Pit::getValue).sum();
        int pitSum = getSimplePitsStream(game).sum();
        int kalahSum = getKalahValuesStream(game).sum();
        boolean pitsEqualsStoneCount = getSimplePitsStream(game).allMatch(p -> p == game.getStoneCount());
        boolean kalahsEqualsZero = getKalahValuesStream(game).allMatch(p -> p == 0);
        assertEquals(72, sum);
        assertEquals(72, pitSum);
        assertEquals(0, kalahSum);
        assertTrue(pitsEqualsStoneCount);
        assertTrue(kalahsEqualsZero);
    }

    @Test
    public void moveSimple() {
        Game game = getGame();
        game.move(1);
        Pit[] pits = game.getPits();
        assertEquals(72, Arrays.stream(pits).mapToInt(Pit::getValue).sum());
        assertEquals(0, pits[0].getValue());
    }

    @Test
    public void moveFreeTurn() {
        Game game = getGame();
        game.move(1);
        assertEquals(Player.FIRST, game.getCurrentPlayer());
        Pit[] pits = game.getPits();
        assertEquals(72, Arrays.stream(pits).mapToInt(Pit::getValue).sum());
        assertEquals(1, pits[6].getValue());
        assertTrue(game.isFreeTurn());
    }

    @Test
    public void moveInvalidTurn() {
        Game game = getGame();
        game.move(2);
        GameException gameException = assertThrows(GameException.class, () -> game.move(3));
        assertEquals(ErrorDefinition.OPPONENT_TURN.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveInvalidTurnAfterFreeTurn() {
        Game game = getGame();
        game.move(1);
        GameException gameException = assertThrows(GameException.class, () -> game.move(9));
        assertEquals(ErrorDefinition.OPPONENT_TURN.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveInvalidPitIdBig() {
        Game game = getGame();
        GameException gameException = assertThrows(GameException.class, () -> game.move(20));
        assertEquals(ErrorDefinition.INVALID_PITID.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveInvalidPitIdNegative() {
        Game game = getGame();
        GameException gameException = assertThrows(GameException.class, () -> game.move(-20));
        assertEquals(ErrorDefinition.INVALID_PITID.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveInvalidPitIdKalaFirst() {
        Game game = getGame();
        GameException gameException = assertThrows(GameException.class, () -> game.move(game.getPitCount() / 2));
        assertEquals(ErrorDefinition.INVALID_PITID.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveInvalidPitIdKalaSeconds() {
        Game game = getGame();
        GameException gameException = assertThrows(GameException.class, () -> game.move(game.getPitCount()));
        assertEquals(ErrorDefinition.INVALID_PITID.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveEmptyPitId() {
        Game game = getGame();
        game.move(2);
        game.move(8);
        GameException gameException = assertThrows(GameException.class, () -> game.move(2));
        assertEquals(ErrorDefinition.EMPTY_PIT_SELECTED.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveOnOtherKalah() {
        Game game = getGame();
        int[] play = {3, 9, 4, 10, 5, 8, 4};
        Arrays.stream(play).forEach(game::move);
        Pit[] pits = game.getPits();
        assertEquals(72, Arrays.stream(pits).mapToInt(Pit::getValue).sum());
        assertEquals(0, pits[4].getValue());
        assertEquals(0, pits[8].getValue());
        assertEquals(7, pits[6].getValue());
    }

    @Test
    public void moveOverTwoKalah() {
        Game game = getGame();
        int[] play = {6, 8, 5, 11, 4, 12};
        Arrays.stream(play).forEach(game::move);
        Pit[] pits = game.getPits();
        assertEquals(72, Arrays.stream(pits).mapToInt(Pit::getValue).sum());
    }

    @Test
    public void moveOverTwoKalahReverse() {
        Game game = getGame();
        int[] play = {2, 13, 1, 12, 4, 11, 5};
        Arrays.stream(play).forEach(game::move);
        Pit[] pits = game.getPits();
        assertEquals(72, Arrays.stream(pits).mapToInt(Pit::getValue).sum());
    }

    @Test
    public void moveGameOverEndWithFirst() {
        Game game = getGame();
        int[] play = {1, 5, 8, 1, 9, 1, 12, 1, 13, 4, 12, 1, 8, 5, 10, 4, 11, 4, 12, 9, 1, 8, 2, 13, 2, 11, 4, 10, 6, 1};
        Arrays.stream(play).forEach(game::move);
        Pit[] pits = game.getPits();
        int sum = Arrays.stream(pits).mapToInt(Pit::getValue).sum();
        int pitSum = getSimplePitsStream(game).sum();
        int kalahSum = getKalahValuesStream(game).sum();
        assertEquals(72, sum);
        assertEquals(72, kalahSum);
        assertEquals(0, pitSum);
        assertTrue(game.isEnded());
    }

    @Test
    public void moveGameOverEndWithSecond() {
        Game game = getGame();
        int[] play = {1, 5, 8, 1, 9, 1, 12, 1, 13, 4, 12, 1, 8, 5, 10, 4, 11, 4, 12, 9, 1, 8, 2, 13, 2, 11, 4, 12, 5, 10};
        Arrays.stream(play).forEach(game::move);
        Pit[] pits = game.getPits();
        int sum = Arrays.stream(pits).mapToInt(Pit::getValue).sum();
        int pitSum = getSimplePitsStream(game).sum();
        int kalahSum = getKalahValuesStream(game).sum();
        assertEquals(72, sum);
        assertEquals(72, kalahSum);
        assertEquals(0, pitSum);
        assertTrue(game.isEnded());
    }

    @Test
    public void moveAfterGameOver() {
        Game game = getGame();
        int[] play = {1, 5, 8, 1, 9, 1, 12, 1, 13, 4, 12, 1, 8, 5, 10, 4, 11, 4, 12, 9, 1, 8, 2, 13, 2, 11, 4, 10, 6, 1};
        Arrays.stream(play).forEach(game::move);
        GameException gameException = assertThrows(GameException.class, () -> game.move(3));
        assertEquals(ErrorDefinition.GAME_IS_OVER.getMessage(), gameException.getMessage());
    }

    @Test
    public void moveCapture() {
        Game game = getGame();
        int[] play = {4, 12, 5, 8, 4};
        Arrays.stream(play).forEach(game::move);
        Pit[] pits = game.getPits();
        assertEquals(72, Arrays.stream(pits).mapToInt(Pit::getValue).sum());
        assertEquals(12, pits[6].getValue());
        Assert.assertFalse(game.isFreeTurn());
    }

    private IntStream getKalahValuesStream(Game game) {
        return IntStream.range(0, game.getPits().length)
                .filter(i -> i == game.getFirstKalahIndex() || i == game.getSecondKalahIndex())
                .map(i -> game.getPits()[i].getValue());
    }

    private IntStream getSimplePitsStream(Game game) {
        return IntStream.range(0, game.getPits().length)
                .filter(i -> i != game.getFirstKalahIndex() && i != game.getSecondKalahIndex())
                .map(i -> game.getPits()[i].getValue());
    }
}
