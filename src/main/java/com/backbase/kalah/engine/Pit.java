package com.backbase.kalah.engine;

import lombok.Getter;

import java.io.Serializable;

/**
 * Represent a pit contains {@link Pit#value} number of stones
 */
@Getter
public class Pit implements Serializable {

    private int value;

    public Pit(int value) {
        this.value = value;
    }

    public int clear() {
        int pre = value;
        value = 0;
        return pre;
    }

    /**
     * increases the value by count
     *
     * @param count
     */
    public void increase(int count) {
        value += count;
    }

    /**
     * Is this pit a kalah?
     *
     * @return false for default pit
     */
    public boolean isKala() {
        return false;
    }

    /**
     * Is this pit empty?
     *
     * @return true if the pit is empty
     */
    public boolean isEmpty() {
        return value == 0;
    }
}

