package org.trade.fluts.parser;

import java.util.List;

public interface FlutsParser {

    Integer parseSchuurNumber(String input);

    List<Integer> parseFlutsPile(String input);
}
