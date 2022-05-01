package emented.lab7.server.clientcommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class RemoveByIdCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public RemoveByIdCommand(CommandProcessor commandProcessor) {
        super("remove_by_id",
                1,
                "delete a group from a collection by its id",
                "id");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.removeById(request);
    }
}
