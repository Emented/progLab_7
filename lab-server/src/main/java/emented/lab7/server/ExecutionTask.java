package emented.lab7.server;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.RequestType;
import emented.lab7.common.util.Response;
import emented.lab7.server.util.CommandManager;
import emented.lab7.server.util.UsersManager;

import java.util.concurrent.Callable;

public class ExecutionTask implements Callable<Response> {

    private final Request request;
    private final CommandManager commandManager;
    private final UsersManager usersManager;

    public ExecutionTask(Request request, CommandManager commandManager, UsersManager usersManager) {
        this.request = request;
        this.commandManager = commandManager;
        this.usersManager = usersManager;
    }

    public Response call() {
        if (request.getRequestType().equals(RequestType.COMMAND)) {
            return commandManager.executeClientCommand(request);
        } else if (request.getRequestType().equals(RequestType.REGISTER)) {
            return usersManager.registerNewUser(request);
        } else {
            return usersManager.loginUser(request);
        }
    }
}
