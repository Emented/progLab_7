package emented.lab7.server;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.util.CommandManager;
import emented.lab7.server.util.ServerSocketWorker;

import java.io.IOException;


public class RequestThread extends Thread {

    private final ServerSocketWorker serverSocketWorker;
    private final CommandManager commandManager;

    public RequestThread(ServerSocketWorker serverSocketWorker, CommandManager commandManager) {
        this.serverSocketWorker = serverSocketWorker;
        this.commandManager = commandManager;
    }

    @Override
    public void run() {
        while (ServerConfig.getRunning()) {
            try {
                Request acceptedRequest = serverSocketWorker.listenForRequest();
                if (acceptedRequest != null) {
                    Response responseToSend = commandManager.executeClientCommand(acceptedRequest);
                    serverSocketWorker.sendResponse(responseToSend);
                }
            } catch (ClassNotFoundException e) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while deserializing the request, try again"));
            } catch (IOException e) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            }
        }
        try {
            serverSocketWorker.stopServer();
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred during stopping the server"));
        }
    }
}
