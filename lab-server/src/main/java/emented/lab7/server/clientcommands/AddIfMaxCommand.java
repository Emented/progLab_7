package emented.lab7.server.clientcommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class AddIfMaxCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public AddIfMaxCommand(CommandProcessor commandProcessor) {
        super("add_if_max",
                0,
                "add a new item to the collection if its value exceeds the value of the largest item in this collection");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.addIfMax(request);
    }
}
