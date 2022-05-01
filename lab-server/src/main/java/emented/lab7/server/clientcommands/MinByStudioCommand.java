package emented.lab7.server.clientcommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class MinByStudioCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public MinByStudioCommand(CommandProcessor commandProcessor) {
        super("min_by_studio",
                0,
                "output any object from the collection whose studio field value is minimal");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.minByStudio(request);
    }
}
