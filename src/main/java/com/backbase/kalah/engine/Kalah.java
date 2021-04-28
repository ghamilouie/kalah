package com.backbase.kalah.engine;

import lombok.Getter;

/**
 * A sub-class of {@link Pit} which represents a Kalah.
 */
@Getter
public class Kalah extends Pit {

    public Kalah(int value) {
        super(value);
    }

    @Override
    public boolean isKala() {
        return true;
    }
}

