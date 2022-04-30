package emented.lab7.server;

import com.google.common.util.concurrent.AsyncCallable;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.RequestType;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.db.DBSSHConnector;
import emented.lab7.server.util.CommandManager;
import emented.lab7.server.util.ServerSocketWorker;
import emented.lab7.server.util.UsersManager;
import jdk.jfr.internal.instrument.ThrowableTracer;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;


public class RequestThread implements Runnable {

    private final ServerSocketWorker serverSocketWorker;
    private final CommandManager commandManager;
    private final UsersManager usersManager;
    private final ExecutorService fixedService = Executors.newFixedThreadPool(5);
    private final ExecutorService cachedService = Executors.newCachedThreadPool();
    ListeningExecutorService listeningCachedService = MoreExecutors.listeningDecorator(cachedService);
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(4);
    ListeningExecutorService listeningForkJoinService = MoreExecutors.listeningDecorator(forkJoinPool);

    public RequestThread(ServerSocketWorker serverSocketWorker, CommandManager commandManager, UsersManager usersManager) {
        this.serverSocketWorker = serverSocketWorker;
        this.commandManager = commandManager;
        this.usersManager = usersManager;
    }

    @Override
    public void run() {
        while (ServerConfig.getRunning()) {
            try {
                Future<Request> listenFuture = fixedService.submit(serverSocketWorker::listenForRequest);
                Request acceptedRequest = listenFuture.get();
                if (acceptedRequest != null) {
                    Callable<Response> executionTask = new ExecutionTask(acceptedRequest, commandManager, usersManager);
                    AsyncCallable<Response> asyncCallable = Callables.asAsyncCallable(executionTask, listeningCachedService);
                    ListenableFuture<Response> listenableFuture = Futures.submitAsync(asyncCallable, listeningCachedService);
                    Futures.addCallback(listenableFuture,
                            new FutureCallback<Response>() {
                                public void onSuccess(Response response) {
                                    try {
                                        serverSocketWorker.sendResponse(response);
                                    } catch (IOException e) {
                                        ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
                                    }
                                }

                                public void onFailure(Throwable thrown) {
                                    thrown.getCause();
                                }
                            },
                            listeningForkJoinService);
                }
            } catch (ExecutionException e) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            } catch (InterruptedException e) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while deserializing the request, try again"));
            }
        }
        try {
            serverSocketWorker.stopServer();
            DBSSHConnector.closeSSH();
            listeningCachedService.shutdown();
            listeningForkJoinService.shutdown();
            fixedService.shutdown();
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred during stopping the server"));
        }
    }
}
