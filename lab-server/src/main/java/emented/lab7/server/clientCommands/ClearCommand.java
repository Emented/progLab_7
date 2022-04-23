package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class ClearCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public ClearCommand(CommandProcessor commandProcessor) {
        super("clear",
                0,
                "clear the collection");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.clear(request);
    }
}
