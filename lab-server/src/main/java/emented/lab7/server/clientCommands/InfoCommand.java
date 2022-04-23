package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class InfoCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;
    public InfoCommand(CommandProcessor commandProcessor) {
        super("info",
                0,
                "display information about the collection");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.info(request);
    }
}
