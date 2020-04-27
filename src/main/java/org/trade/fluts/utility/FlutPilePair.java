package org.trade.fluts.utility;

import lombok.Getter;

/**
 * FlutsPilePAir class used to save the number and all fluts price
 *
 * @param <L>
 * @param <R>
 */
public class FlutPilePair<L, R> {

    @Getter
    private final L flutsCount;

    @Getter
    private final R flutsPile;

    public FlutPilePair(L flutsCount, R flutsPile) {
        this.flutsCount = flutsCount;
        this.flutsPile = flutsPile;
    }
}
