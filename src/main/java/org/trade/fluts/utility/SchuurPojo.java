package org.trade.fluts.utility;

import lombok.Data;

import java.util.TreeSet;

/**
 * POJO class containing the schuur count, schuur profit and all fluts counts you should buy
 */
@Data
public class SchuurPojo {

    private int schuursCount;
    private int schuurProfit;
    private TreeSet<Integer> flutsCountsToBuy;
}
