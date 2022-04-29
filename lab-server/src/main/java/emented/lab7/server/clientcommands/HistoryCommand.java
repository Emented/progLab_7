package emented.lab7.server.clientcommands;


import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CommandProcessor;

import java.util.ArrayDeque;

public class HistoryCommand extends AbstractClientCommand {

    private final ArrayDeque<String> queueOfCommands;
    private final CommandProcessor commandProcessor;


    public HistoryCommand(ArrayDeque<String> queueOfCommands, CommandProcessor commandProcessor) {
        super("history",
                0,
                "output the last 9 commands");
        this.queueOfCommands = queueOfCommands;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.history(request, queueOfCommands);
    }
}
