package emented.lab7.server.serverCommands;

import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;
import emented.lab7.server.abstractions.AbstractServerCommand;
import emented.lab7.server.parser.XMLParser;
import emented.lab7.server.util.CollectionManager;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerExitCommand extends AbstractServerCommand {

    private final Scanner scanner;
    private final XMLParser parser;
    private final CollectionManager collectionManager;

    public ServerExitCommand(Scanner scanner, XMLParser parser, CollectionManager collectionManager) {
        super("exit", "shut down the server (you'll be asked to store all the changes)");
        this.scanner = scanner;
        this.parser = parser;
        this.collectionManager = collectionManager;
    }

    @Override
    public String executeServerCommand() {
        chooseSaving();
        ServerConfig.toggleRun();
        return TextColoring.getGreenText("Server shutdown");
    }

    private void chooseSaving() {
        ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Do you want to save changes? [y/n]"));
        try {
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("You lost all of your data )="));
            } else if ("y".equals(s)) {
                parser.writeToXMLofExistingInstance(collectionManager);
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Collection was successfully saved"));
            } else {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("You entered not valid symbol, try again"));
                chooseSaving();
            }
        } catch (NoSuchElementException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
            System.exit(1);
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
        }
    }
}
