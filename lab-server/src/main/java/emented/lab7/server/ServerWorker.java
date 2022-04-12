package emented.lab7.server;

import com.thoughtworks.xstream.converters.ConversionException;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.clientCommands.AddCommand;
import emented.lab7.server.clientCommands.AddIfMaxCommand;
import emented.lab7.server.clientCommands.ClearCommand;
import emented.lab7.server.clientCommands.CountLessThatNumberOfParticipantsCommand;
import emented.lab7.server.clientCommands.ExecuteScriptCommand;
import emented.lab7.server.clientCommands.ExitCommand;
import emented.lab7.server.clientCommands.HelpCommand;
import emented.lab7.server.clientCommands.HistoryCommand;
import emented.lab7.server.clientCommands.InfoCommand;
import emented.lab7.server.clientCommands.MinByStudioCommand;
import emented.lab7.server.clientCommands.RemoveAnyByNumberOfParticipantsCommand;
import emented.lab7.server.clientCommands.RemoveByIdCommand;
import emented.lab7.server.clientCommands.RemoveGreaterCommand;
import emented.lab7.server.clientCommands.ShowCommand;
import emented.lab7.server.clientCommands.UpdateCommand;
import emented.lab7.server.parser.XMLParser;
import emented.lab7.server.serverCommands.ServerExitCommand;
import emented.lab7.server.serverCommands.ServerHelpCommand;
import emented.lab7.server.serverCommands.ServerHistoryCommand;
import emented.lab7.server.serverCommands.ServerSaveCommand;
import emented.lab7.server.util.CollectionManager;
import emented.lab7.server.util.CommandManager;
import emented.lab7.server.util.ServerCommandListener;
import emented.lab7.server.util.ServerSocketWorker;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerWorker {

    private final Scanner scanner = new Scanner(System.in);
    private final int maxPort = 65535;
    private ServerSocketWorker serverSocketWorker;
    private final String fileName;
    private final ServerCommandListener serverCommandListener = new ServerCommandListener(scanner);
    private CollectionManager collectionManager;
    private CommandManager commandManager;
    private final XMLParser parser = new XMLParser();


    public ServerWorker(String fileName) {
        this.fileName = fileName;
    }

    public void startServerWorker() {
        try {
            collectionManager = parser.readFromXML(this.fileName);
            commandManager = new CommandManager(
                    new HelpCommand(ServerConfig.getClientAvailableCommands()),
                    new InfoCommand(collectionManager),
                    new ShowCommand(collectionManager),
                    new AddCommand(collectionManager),
                    new UpdateCommand(collectionManager),
                    new RemoveByIdCommand(collectionManager),
                    new ClearCommand(collectionManager),
                    new ExitCommand(),
                    new AddIfMaxCommand(collectionManager),
                    new RemoveGreaterCommand(collectionManager),
                    new HistoryCommand(ServerConfig.getClientCommandHistory().getHistory()),
                    new RemoveAnyByNumberOfParticipantsCommand(collectionManager),
                    new MinByStudioCommand(collectionManager),
                    new CountLessThatNumberOfParticipantsCommand(collectionManager),
                    new ExecuteScriptCommand(),
                    new ServerHelpCommand(ServerConfig.getServerAvailableCommands()),
                    new ServerExitCommand(scanner, parser, collectionManager),
                    new ServerSaveCommand(collectionManager, parser),
                    new ServerHistoryCommand(ServerConfig.getClientCommandHistory().getHistory()));
            inputPort();
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Welcome to the server! To see the list of commands input HELP"));
            RequestThread requestThread = new RequestThread(serverSocketWorker, commandManager);
            ConsoleThread consoleThread = new ConsoleThread(serverCommandListener, commandManager);
            requestThread.start();
            consoleThread.start();
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
        } catch (ConversionException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Error during type conversion"));
            System.exit(1);
        }
    }

    private void inputPort() throws IOException {
        ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Do you want to use a default port? [y/n]"));
        try {
            String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if ("n".equals(s)) {
                while (true) {
                    ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Please enter the remote host port (1-65535)"));
                    String port = scanner.nextLine();
                    try {
                        int portInt = Integer.parseInt(port);
                        if (portInt > 0 && portInt <= maxPort) {
                            serverSocketWorker = new ServerSocketWorker(portInt);
                            break;
                        } else {
                            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The number did not fall within the limits, repeat the input"));
                        }
                    } catch (IllegalArgumentException e) {
                        ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Error processing the number, repeat the input"));
                    }
                }
            } else if ("y".equals(s)) {
                serverSocketWorker = new ServerSocketWorker();
            } else {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("You entered not valid symbol, try again"));
                inputPort();
            }
        } catch (NoSuchElementException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
            System.exit(1);
        }
    }
}
