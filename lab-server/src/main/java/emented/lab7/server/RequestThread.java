package emented.lab7.server;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.RequestType;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.db.DBSSHConnector;
import emented.lab7.server.util.CommandManager;
import emented.lab7.server.util.ServerSocketWorker;
import emented.lab7.server.util.UsersManager;

import java.io.IOException;


public class RequestThread extends Thread {

    private final ServerSocketWorker serverSocketWorker;
    private final CommandManager commandManager;
    private final UsersManager usersManager;

    public RequestThread(ServerSocketWorker serverSocketWorker, CommandManager commandManager, UsersManager usersManager) {
        this.serverSocketWorker = serverSocketWorker;
        this.commandManager = commandManager;
        this.usersManager = usersManager;
    }

    @Override
    public void run() {
        while (ServerConfig.getRunning()) {
            try {
                Request acceptedRequest = serverSocketWorker.listenForRequest();
                if (acceptedRequest != null) {
                    Response responseToSend;
                    if (acceptedRequest.getRequestType().equals(RequestType.COMMAND)) {
                        responseToSend = commandManager.executeClientCommand(acceptedRequest);
                    } else if (acceptedRequest.getRequestType().equals(RequestType.REGISTER)) {
                        responseToSend = usersManager.registerNewUser(acceptedRequest);
                    } else {
                        responseToSend = usersManager.loginUser(acceptedRequest);
                    }
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
            DBSSHConnector.closeSSH();
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred during stopping the server"));
        }
    }
}
