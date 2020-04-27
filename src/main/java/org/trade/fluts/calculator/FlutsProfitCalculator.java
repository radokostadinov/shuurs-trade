package org.trade.fluts.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.trade.fluts.parser.ConsoleInputParser;
import org.trade.fluts.parser.FlutsParser;
import org.trade.fluts.reader.FlutsConsoleReader;
import org.trade.fluts.reader.FlutsReader;
import org.trade.fluts.utility.FlutPilePair;
import org.trade.fluts.utility.SchuurPojo;

import java.util.*;

/**
 * This class can calculate the fluts number you need to buy the best profit
 */
public class FlutsProfitCalculator {

    private static final Logger LOGGER = LogManager.getLogger(FlutsProfitCalculator.class);

    private static final int FLUTS_SELL_PRICE = 10;

    private static final String NUMBER_MISMATCH_ERROR_MESSAGE = "The total number of values does not match!";
    private static final String ZERO_VALUE_NOT_ALLOWED_ERROR_MESSAGE = "Flut price equals to 0 is not allowed!";

    private static final String INPUT_SCHUUR_NUMBER_MESSAGE = "Enter the number of the schuurs:";
    private static final String INPUT_FLUTS_PILE_MESSAGE = "Enter the fluts pile:";

    // Map with all schuur cases, containing the number of schuurs as a key and all buying fluts price for a specific case
    private Map<Integer, List<List<Integer>>> schuurFlutCases = new LinkedHashMap<>();

    private FlutsReader consoleReader = new FlutsConsoleReader();

    private FlutsParser inputParser = new ConsoleInputParser();

    /**
     * Start point of the Fluts Calculator
     */
    public void startCalculator() {


        do {

            // Get the number of the schuurs for the current case
            int schuursCount = getSchuursCount();

            // if the schuur number is 0, then we will calculate the exact profit
            if (schuursCount == 0) {
                String calculateProfitMessage = "Zero schuur was entered. Now will calculate the profit!";

                LOGGER.info(calculateProfitMessage);
                calculateProfit();

                break;
            }

            List<List<Integer>> schuurFluts = new ArrayList<>();
            for (int currentSchuur = 0; currentSchuur < schuursCount; currentSchuur++) {

                schuurFluts.add(addFluts());
            }

            schuurFlutCases.put(schuursCount, schuurFluts);

        } while (true);
    }


    /**
     * Calculate the number of fluts to buy and the expected profit
     */
    private void calculateProfit() {

        // This list will contain the profit and the total number of fluts you should buy
        List<SchuurPojo> profits = new ArrayList<>();

        for (Map.Entry<Integer, List<List<Integer>>> entry : schuurFlutCases.entrySet()) {

            // the total profit you can obtain for all schuur
            int totalProfit = 0;

            // all fluts count you should buy, to obtain the best profit
            List<Integer> flutsCountToBuy = new LinkedList<>();

            for (List<Integer> schuurNumber : entry.getValue()) {

                // total sum from all fluts for the current schuur
                int sum = 0;

                // the  profit you can obtain for the current schuur
                int currentProfit;

                // current profit for the schuur
                int bestCurrentProfit = 0;
                // all fluts count you should buy, to obtain the best profit for the current schuur
                List<Integer> currentFlutsNumberToBuy = new LinkedList<>();

                for (int flutNumber = 0; flutNumber < schuurNumber.size(); flutNumber++) {

                    sum += schuurNumber.get(flutNumber);
                    currentProfit = (FLUTS_SELL_PRICE * (flutNumber + 1)) - (sum);

                    // check the state of the profit and if there are another fluts counts for the same profit
                    if (currentProfit > bestCurrentProfit) {
                        currentFlutsNumberToBuy.clear();
                        currentFlutsNumberToBuy.add(flutNumber + 1);
                        bestCurrentProfit = currentProfit;
                    } else if (currentProfit == bestCurrentProfit) {
                        currentFlutsNumberToBuy.add(flutNumber + 1);
                        bestCurrentProfit = currentProfit;
                    }
                }

                // add the current profit, to the total profit
                totalProfit += bestCurrentProfit;

                // in case there are more schuurs, adjust the fluts count to buy
                flutsCountToBuy = adjustSchuurFlutCounts(flutsCountToBuy, currentFlutsNumberToBuy);
            }

            profits.add(addCurrentFlutsCase(entry.getValue().size(), totalProfit, flutsCountToBuy));
        }

        // print the schuur count, fluts profit and fluts count for all cases
        printExpectedProfits(profits);
    }

    /**
     * Return schuur POJO with the containing shuur size, profit and fluts number to buy
     */
    private SchuurPojo addCurrentFlutsCase(int schuurSize, int totalProfit, List<Integer> flutsCountToBuy) {
        SchuurPojo flutsCase = new SchuurPojo();
        flutsCase.setSchuursCount(schuurSize);
        flutsCase.setSchuurProfit(totalProfit);
        if (flutsCountToBuy.size() >= 10) {
            flutsCase.setFlutsCountsToBuy(new TreeSet<>(flutsCountToBuy.subList(0, 10)));
        } else {
            flutsCase.setFlutsCountsToBuy(new TreeSet<>(flutsCountToBuy));
        }
        return flutsCase;
    }

    /**
     * Adjust the fluts count to buy whenever there are more than one schuurs
     *
     * @param currentFlutsCountList the current list with fluts count
     * @param newFlutsCountList     list with the new fluts count to buy
     * @return a new list with the adjusted fluts count to buy
     */
    private List<Integer> adjustSchuurFlutCounts(List<Integer> currentFlutsCountList, List<Integer> newFlutsCountList) {

        // this list will contain the newly added and adjusted fluts counts
        List<Integer> adjustedFlutsCountList = new LinkedList<>();

        // if the current list is empty, just return the list with all new fluts
        if (currentFlutsCountList.isEmpty()) {

            return newFlutsCountList;
        } else {
            for (Integer newFlutsCount : newFlutsCountList) {
                for (Integer currentFlutCount : currentFlutsCountList) {
                    adjustedFlutsCountList.add(currentFlutCount + newFlutsCount);
                }
            }
        }

        return adjustedFlutsCountList;
    }

    /**
     * Prints the number of the schuurs, the profit and all possible fluts you can buy to obtain it for all cases
     *
     * @param profits map with all the profits and fluts counts to buy
     */
    private void printExpectedProfits(List<SchuurPojo> profits) {
        for (SchuurPojo entity : profits) {
            System.out.println("Schuurs " + entity.getSchuursCount());
            System.out.println("Maximum profit is  " + entity.getSchuurProfit());
            System.out.println("Number of fluts to buy: " + entity.getFlutsCountsToBuy().toString());
            System.out.println();
        }
    }

    /**
     * Add all fluts for a single schuur in a {@link List}
     *
     * @return {@link List} with all fluts
     */
    private List<Integer> addFluts() {

        LOGGER.info(INPUT_FLUTS_PILE_MESSAGE);
        FlutPilePair<String, String> loginFlutPilePair = consoleReader.getFlutsPile();

        int flutsCount = inputParser.parseSchuurNumber(loginFlutPilePair.getFlutsCount());

        List<Integer> flutsPileList = inputParser.parseFlutsPile(loginFlutPilePair.getFlutsPile());

        verifyFlutsPileInput(flutsCount, flutsPileList);

        return flutsPileList;
    }

    /**
     * This method verify if the fluts pile is the exact length and if there are any zero values
     *
     * @param flutsCount    length of the fluts pile
     * @param flutsPileList list with all fluts pile
     */
    private void verifyFlutsPileInput(int flutsCount, List<Integer> flutsPileList) {
        if (flutsPileList.size() != flutsCount) {
            LOGGER.error(NUMBER_MISMATCH_ERROR_MESSAGE);
            throw new IllegalArgumentException(NUMBER_MISMATCH_ERROR_MESSAGE);
        }

        LOGGER.debug("Check for zero values in the fluts pile.");
        boolean exists = flutsPileList.stream().anyMatch(x -> Objects.equals(x, 0));
        if (exists) {
            LOGGER.error(ZERO_VALUE_NOT_ALLOWED_ERROR_MESSAGE);
            throw new IllegalArgumentException(ZERO_VALUE_NOT_ALLOWED_ERROR_MESSAGE);
        }
    }

    /**
     * Prompt the user to enter the number of the schuurs for the current test case
     *
     * @return schuur count
     */
    private int getSchuursCount() {

        LOGGER.info(INPUT_SCHUUR_NUMBER_MESSAGE);
        String consoleInput = consoleReader.getNumberOfSchuurs();

        return inputParser.parseSchuurNumber(consoleInput);
    }
}
