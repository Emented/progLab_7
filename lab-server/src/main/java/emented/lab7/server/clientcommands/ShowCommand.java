package emented.lab7.server.clientcommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class ShowCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public ShowCommand(CommandProcessor commandProcessor) {
        super("show",
                0,
                "display all the elements of the collection and information about them");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.show(request);
    }
}
