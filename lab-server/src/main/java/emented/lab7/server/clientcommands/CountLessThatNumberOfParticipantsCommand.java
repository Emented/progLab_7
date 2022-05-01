package emented.lab7.server.clientcommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

public class CountLessThatNumberOfParticipantsCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public CountLessThatNumberOfParticipantsCommand(CommandProcessor commandProcessor) {
        super("count_less_than_number_of_participants",
                1,
                "print the number of groups whose number of participants is less than the specified one",
                "number of participants");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.countLessThenNumberOfParticipants(request);
    }
}
