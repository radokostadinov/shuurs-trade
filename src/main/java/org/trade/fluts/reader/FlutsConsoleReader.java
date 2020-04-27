package org.trade.fluts.reader;

import org.trade.fluts.utility.FlutPilePair;

import java.util.Scanner;

/**
 * Fluts console reader used to get the entered input values for fluts schuurs and piles from the console
 */
public class FlutsConsoleReader implements FlutsReader {

    // Scanner object for the user input
    private Scanner consoleInput = new Scanner(System.in);

    /**
     * Get the number of the schuurs from the console
     */
    @Override
    public String getNumberOfSchuurs() {

        return consoleInput.nextLine();
    }

    /**
     * Get the fluts pile  from the console
     */
    @Override
    public FlutPilePair<String, String> getFlutsPile() {

        String flutsNumber = consoleInput.next();
        String flutsPile = consoleInput.nextLine();

        return new FlutPilePair<>(flutsNumber, flutsPile);
    }
}
