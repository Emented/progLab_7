package emented.lab7.server;

import emented.lab7.common.exceptions.DatabaseException;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.clientcommands.AddCommand;
import emented.lab7.server.clientcommands.AddIfMaxCommand;
import emented.lab7.server.clientcommands.ClearCommand;
import emented.lab7.server.clientcommands.CountLessThatNumberOfParticipantsCommand;
import emented.lab7.server.clientcommands.ExecuteScriptCommand;
import emented.lab7.server.clientcommands.ExitCommand;
import emented.lab7.server.clientcommands.HelpCommand;
import emented.lab7.server.clientcommands.HistoryCommand;
import emented.lab7.server.clientcommands.InfoCommand;
import emented.lab7.server.clientcommands.MinByStudioCommand;
import emented.lab7.server.clientcommands.RemoveAnyByNumberOfParticipantsCommand;
import emented.lab7.server.clientcommands.RemoveByIdCommand;
import emented.lab7.server.clientcommands.RemoveGreaterCommand;
import emented.lab7.server.clientcommands.ShowCommand;
import emented.lab7.server.clientcommands.UpdateCommand;
import emented.lab7.server.db.DBManager;
import emented.lab7.server.db.DBSSHConnector;
import emented.lab7.server.interfaces.DBConnectable;
import emented.lab7.server.servercommands.ServerExitCommand;
import emented.lab7.server.servercommands.ServerHelpCommand;
import emented.lab7.server.servercommands.ServerHistoryCommand;
import emented.lab7.server.util.CollectionManager;
import emented.lab7.server.util.CommandManager;
import emented.lab7.server.util.CommandProcessor;
import emented.lab7.server.util.ServerCommandListener;
import emented.lab7.server.util.ServerSocketWorker;
import emented.lab7.server.util.UsersManager;

import java.io.IOException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerWorker {

    private final Scanner scanner = new Scanner(System.in);
    private final int maxPort = 65535;
    private final ServerCommandListener serverCommandListener = new ServerCommandListener(scanner);
    private final DBConnectable dbConnector = new DBSSHConnector();
    private ServerSocketWorker serverSocketWorker;
    private final CollectionManager collectionManager = new CollectionManager();
    private UsersManager usersManager;
    private DBManager dbManager;
    private CommandProcessor commandProcessor;
    private CommandManager commandManager;

    public void startServerWorker() {
        try {
            dbManager = new DBManager(dbConnector);
            usersManager = new UsersManager(dbManager);
            commandProcessor = new CommandProcessor(dbManager, collectionManager);
            collectionManager.setMusicBands(dbManager.loadCollection());
            commandManager = new CommandManager(
                    new HelpCommand(ServerConfig.getClientAvailableCommands(), commandProcessor),
                    new InfoCommand(commandProcessor),
                    new ShowCommand(commandProcessor),
                    new AddCommand(commandProcessor),
                    new UpdateCommand(commandProcessor),
                    new RemoveByIdCommand(commandProcessor),
                    new ClearCommand(commandProcessor),
                    new ExitCommand(),
                    new AddIfMaxCommand(commandProcessor),
                    new RemoveGreaterCommand(commandProcessor),
                    new HistoryCommand(ServerConfig.getClientCommandHistory().getHistory(), commandProcessor),
                    new RemoveAnyByNumberOfParticipantsCommand(commandProcessor),
                    new MinByStudioCommand(commandProcessor),
                    new CountLessThatNumberOfParticipantsCommand(commandProcessor),
                    new ExecuteScriptCommand(),
                    new ServerHelpCommand(ServerConfig.getServerAvailableCommands()),
                    new ServerExitCommand(),
                    new ServerHistoryCommand(ServerConfig.getClientCommandHistory().getHistory()));
            inputPort();
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Welcome to the server! To see the list of commands input HELP"));
            Thread requestThread = new Thread(new RequestThread(serverSocketWorker, commandManager, usersManager));
            Thread consoleThread = new Thread(new ConsoleThread(serverCommandListener, commandManager));
            requestThread.start();
            consoleThread.start();
        } catch (IOException | DatabaseException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
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
