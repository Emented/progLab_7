package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;

public class ExitCommand extends AbstractClientCommand {

    public ExitCommand() {
        super("exit", 0, "shut down the client (all your changes will be lost)");
    }

    @Override
    public Response executeClientCommand(Request request) {
        return null;
    }
}
