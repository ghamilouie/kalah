package com.backbase.kalah.engine;

import java.util.Arrays;

/**
 * Represents a turn in game ans is responsible for calculating player related index and cases.
 */
public enum Player {

    FIRST, SECOND;

    public static Player getPlayer(int index, int pitCount) {
        return (index >= 0 && index <= firstKalahIndex(pitCount)) ? FIRST : SECOND;
    }

    private static int firstKalahIndex(int pitCount) {
        return pitCount / 2 - 1;

    }

    private static int secondKalahIndex(int pitCount) {
        return pitCount - 1;

    }

    public Player getOpponent() {
        return this == FIRST ? SECOND : FIRST;
    }

    public boolean isOpponentKala(int index, int pitCount) {
        if (this == FIRST) {
            return index == secondKalahIndex(pitCount);
        } else {
            return index == firstKalahIndex(pitCount);
        }
    }

    public int getKalaId(int pitCount) {
        return this == FIRST ? firstKalahIndex(pitCount) : secondKalahIndex(pitCount);
    }

    public <T> T[] getPits(T[] pits, int pitCount) {
        return this == FIRST ? Arrays.copyOfRange(pits, 0, firstKalahIndex(pitCount)) :
                Arrays.copyOfRange(pits, firstKalahIndex(pitCount) + 1, secondKalahIndex(pitCount));
    }

    public boolean isMyPit(int index, int pitCount) {
        return (this == FIRST) == (index <= firstKalahIndex(pitCount));
    }
}
