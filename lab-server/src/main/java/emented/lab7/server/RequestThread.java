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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;


public class RequestThread implements Runnable {

    private final ServerSocketWorker serverSocketWorker;
    private final CommandManager commandManager;
    private final UsersManager usersManager;
    private final ExecutorService fixedService = Executors.newFixedThreadPool(1);
    private final ExecutorService cachedService = Executors.newCachedThreadPool();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(2);

    public RequestThread(ServerSocketWorker serverSocketWorker, CommandManager commandManager, UsersManager usersManager) {
        this.serverSocketWorker = serverSocketWorker;
        this.commandManager = commandManager;
        this.usersManager = usersManager;
    }

    @Override
    public void run() {
//        while (ServerConfig.getRunning()) {
            CompletableFuture
                    .supplyAsync(() -> {
                        try {
                            Request request = serverSocketWorker.listenForRequest();
                            while (request == null) {
                                System.out.println("n");
                                request = serverSocketWorker.listenForRequest();
                                System.out.println("n");
                            }
                            return request;
//                            return new Request("help", RequestType.COMMAND);
                        } catch (IOException e) {
                            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
                            return null;
                        } catch (ClassNotFoundException e) {
                            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while deserializing the request, try again"));
                            return null;
                        }
                    }, fixedService)
                    .thenApplyAsync((Request request) -> {
                        if (request != null) {
                            if (request.getRequestType().equals(RequestType.COMMAND)) {
                                System.out.println("n");
                                return commandManager.executeClientCommand(request);
                            } else if (request.getRequestType().equals(RequestType.REGISTER)) {
                                return usersManager.registerNewUser(request);
                            } else {
                                return usersManager.loginUser(request);
                            }
                        } else {
                            return null;
                        }
                    }, cachedService)
                    .thenAcceptAsync((Response response) -> {
                        if (response != null) {
                            try {
                                System.out.println("n");
                                serverSocketWorker.sendResponse(response);
                            } catch (IOException e) {
                                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
                            }
                        }
                    }, forkJoinPool);
//        }
        try {
            serverSocketWorker.stopServer();
            DBSSHConnector.closeSSH();
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred during stopping the server"));
        }
    }
}
