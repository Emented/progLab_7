package emented.lab7.server.util;

import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerCommandListener {

    private final Scanner sc;

    public ServerCommandListener(Scanner sc) {
        this.sc = sc;
    }

    public String readCommand() {
        try {
            ServerConfig.getConsoleTextPrinter().printText(TextColoring.getBlueText("Enter a command: "));
            return sc.nextLine().trim().toLowerCase(Locale.ROOT);
        } catch (NoSuchElementException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
            System.exit(1);
            return null;
        }
    }
}
