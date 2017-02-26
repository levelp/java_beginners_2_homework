package org.levelp;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Anastasiya Solodkaya.
 */
public class IntRange {
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // Небезопасно!
        if (i > upper.get()) {
            throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
        }
        lower.set(i);
    }

    public void setUpper(int i) {
        // Небезопасно!
        if (i < lower.get()) {
            throw new IllegalArgumentException(
                    "Can't set upper to " + i + " < lower");
        }
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}
