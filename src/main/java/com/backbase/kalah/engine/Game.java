package com.backbase.kalah.engine;

import com.backbase.kalah.exception.ErrorDefinition;
import com.backbase.kalah.exception.GameException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a Kala game and implements the basic rules of the game.
 */
@Log
@Getter
public class Game {

    private final int pitCount;
    private final int stoneCount;
    private final int firstKalahIndex;
    private final int secondKalahIndex;
    @Setter
    private Long id;
    @Setter
    private Pit[] pits;
    @Setter
    private Player currentPlayer;
    @Setter
    private boolean freeTurn = false;
    @Setter
    private boolean ended = false;

    /**
     * Creates a {@link Game} instance with starting state (pit values)
     *
     * @param pitCount   total number of count
     * @param stoneCount number of stones per pit
     */
    public Game(int pitCount, int stoneCount) {
        this.pitCount = pitCount;
        this.stoneCount = stoneCount;
        this.firstKalahIndex = pitCount / 2 - 1;
        this.secondKalahIndex = pitCount - 1;
        this.pits = new Pit[pitCount];
        for (int i = 0; i < pitCount; i++) {
            if (i == firstKalahIndex || i == secondKalahIndex) {
                this.pits[i] = new Kalah(0);
            } else {
                this.pits[i] = new Pit(stoneCount);
            }
        }
        this.currentPlayer = Player.FIRST;
    }

    /**
     * Checks if the index is valid for move or not.
     *
     * @param index pit index to move (0-based index)
     * @throws GameException with detailed information if the move is not valid
     */
    private void checkMove(int index) {
        Player player = Player.getPlayer(index, pitCount);
        if (index == firstKalahIndex || index == secondKalahIndex || index < 0 || index > pitCount - 1) {
            throw new GameException(ErrorDefinition.INVALID_PITID);
        }
        if (ended) {
            throw new GameException(ErrorDefinition.GAME_IS_OVER);
        }
        if (currentPlayer != player) {
            throw new GameException(ErrorDefinition.OPPONENT_TURN);

        }
        if (pits[index].isEmpty()) {
            throw new GameException(ErrorDefinition.EMPTY_PIT_SELECTED);
        }
    }

    /**
     * Performs a move on game and changes the state (value) of pits.
     * If the game ends after the move then {@link Game#ended} is True after move.
     * If the move leads to a free turn then {@link Game#freeTurn} is True after move.
     *
     * @param pitId the pitId to move (1-based)
     * @throws GameException if the move is invalid
     */
    public void move(int pitId) {
        int index = pitId - 1;
        checkMove(index);
        freeTurn = false;
        int lastIndex = runMove(index);
        if (isCollect(lastIndex)) {
            collect(lastIndex);
        }
        if (isEndOfGame()) {
            collectAll();
            ended = true;
            log.info(debugString());
            return;
        }
        if (pits[lastIndex].isKala()) {
            freeTurn = true;
        } else {
            changeTurn();
        }
        log.info(debugString());
    }

    /**
     * Actual execution of a move which changes the state(value) of pits.
     *
     * @param index 0-based pit index to move
     * @return the index of the pit that the last stone of move is placed
     */
    private int runMove(int index) {
        int lastIndex = 0;
        int count = pits[index].clear();
        int i = index + 1;
        while (count > 0) {
            if (currentPlayer.isOpponentKala(i, pitCount)) {
                i = nextIndex(i);
                continue;
            }
            pits[i].increase(1);
            lastIndex = i;
            count--;
            i = nextIndex(i);
        }
        return lastIndex;
    }

    /**
     * Return the next index in the board circle
     *
     * @param i index
     * @return next index after i
     */
    private int nextIndex(int i) {
        return i == pitCount - 1 ? 0 : i + 1;
    }

    /**
     * Checks if all pits are empty or not which represent the end of game.
     *
     * @return {@link true} if the game is ended.
     */
    private boolean isEndOfGame() {
        return Arrays.stream(currentPlayer.getPits(pits, pitCount)).allMatch(Pit::isEmpty) ||
                Arrays.stream(currentPlayer.getOpponent().getPits(pits, pitCount)).allMatch(Pit::isEmpty);
    }

    /**
     * Collects all stones from index and the opposite pit to the currentPlayer kalah.
     *
     * @param index the index of pit to collect
     */
    private void collect(int index) {
        int count = pits[index].clear() + pits[opposite(index)].clear();
        pits[currentPlayer.getKalaId(pitCount)].increase(count);
    }

    /**
     * Returns the opposite pit's index
     *
     * @param index pit index
     * @return opposite pit index
     */
    private int opposite(int index) {
        return pitCount - 2 - index;
    }

    /**
     * Checks if the pit was empty before placing last stone (value==1) and its owned by current player.
     *
     * @param index pit to check
     * @return true if it must be collected
     */
    private boolean isCollect(int index) {
        return currentPlayer.isMyPit(index, pitCount) && !pits[index].isKala() &&
                pits[index].getValue() == 1 && !pits[opposite(index)].isEmpty();
    }

    /**
     * Changes the current player
     */
    private void changeTurn() {
        currentPlayer = currentPlayer.getOpponent();
    }

    /**
     * Collects all remaining stones from pits ans place them into corresponding kalah.
     */
    private void collectAll() {
        Player other = currentPlayer.getOpponent();
        int sumOpponent = Arrays.stream(other.getPits(pits, pitCount)).mapToInt(Pit::clear).sum();
        int sumMine = Arrays.stream(currentPlayer.getPits(pits, pitCount)).mapToInt(Pit::clear).sum();
        pits[other.getKalaId(pitCount)].increase(sumOpponent);
        pits[currentPlayer.getKalaId(pitCount)].increase(sumMine);
    }

    public Map<String, String> getStatusMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < pits.length; i++) {
            map.put(String.valueOf(i + 1), String.valueOf(pits[i].getValue()));
        }
        return map;
    }

    private String debugString() {
        StringBuilder s = new StringBuilder("\n\n----------------------------------------\n");
        for (int i = secondKalahIndex - 1; i > firstKalahIndex; i--) {
            s.append("\t").append(pits[i].getValue());
        }
        s.append("\n").append(pits[secondKalahIndex].getValue())
                .append("\t\t\t\t\t\t\t")
                .append(pits[firstKalahIndex].getValue())
                .append("\t\t").append(currentPlayer).append("\n");
        for (int i = 0; i < firstKalahIndex; i++) {
            s.append("\t").append(pits[i].getValue());
        }
        return s.append("\n----------------------------------------\n").toString();
    }
}
