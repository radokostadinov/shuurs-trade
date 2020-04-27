package org.trade.fluts.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to parse the ser input for the Fluts Calculator
 */
public class ConsoleInputParser implements FlutsParser {

    private static final Logger LOGGER = LogManager.getLogger(ConsoleInputParser.class);

    private static final String EMPTY_INPUT_MESSAGE = "User input is empty!";
    private static final String ONLY_SINGLE_INTEGER_MESSAGE = "The input must be a single integer";
    private static final String INVALID_INPUT_MESSAGE = "The input must be only from integer positive values!";
    private static final String CONVERT_STRING_LIST_MESSAGE = "Convert the String input fluts list  to Integer list";
    private static final String SET_THE_INPUT_FLUTS_TO_STRING_LIST_MESSAGE = "Set the input fluts to String list";

    /**
     * Parse the user input for correctly entered single integer
     *
     * @param input user console input string
     * @return the parsed integer input
     */
    public Integer parseSchuurNumber(String input) {

        if (input.trim().isEmpty()) {
            LOGGER.error(EMPTY_INPUT_MESSAGE);

            throw new IllegalArgumentException(EMPTY_INPUT_MESSAGE);
        }

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException nfe) {
            LOGGER.error(ONLY_SINGLE_INTEGER_MESSAGE);

            throw new IllegalArgumentException(ONLY_SINGLE_INTEGER_MESSAGE);
        }
    }

    /**
     * Fluts pile parser will return integer list with all values from the entered input
     *
     * @param input the console input string
     * @return Integer list with all entered values
     */
    public List<Integer> parseFlutsPile(String input) {

        if (input.trim().isEmpty()) {
            LOGGER.error(EMPTY_INPUT_MESSAGE);

            throw new IllegalArgumentException(EMPTY_INPUT_MESSAGE);
        }

        LOGGER.debug(SET_THE_INPUT_FLUTS_TO_STRING_LIST_MESSAGE);
        List<String> flutsPileStringList = Arrays.asList(input.trim().split(" "));
        List<Integer> flutsPileIntegerList;
        try {
            LOGGER.debug(CONVERT_STRING_LIST_MESSAGE);
            flutsPileIntegerList = flutsPileStringList.stream().map(Integer::valueOf).collect(Collectors.toList());

        } catch (NumberFormatException nfe) {
            LOGGER.error(INVALID_INPUT_MESSAGE);

            throw new IllegalArgumentException(INVALID_INPUT_MESSAGE);
        }

        return flutsPileIntegerList;
    }
}
