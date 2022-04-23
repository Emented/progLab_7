package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class AddCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public AddCommand(CommandProcessor commandProcessor) {
        super("add",
                0,
                "add a new item to the collection");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.add(request);
    }
}
