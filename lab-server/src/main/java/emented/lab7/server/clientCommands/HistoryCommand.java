package emented.lab7.server.clientCommands;


import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;

import java.util.ArrayDeque;

public class HistoryCommand extends AbstractClientCommand {

    private final ArrayDeque<String> queueOfCommands;

    public HistoryCommand(ArrayDeque<String> queueOfCommands) {
        super("history", 0, "output the last 9 commands");
        this.queueOfCommands = queueOfCommands;
    }

    @Override
    public Response executeClientCommand(Request request) {
        StringBuilder sb = new StringBuilder();
        if (!queueOfCommands.isEmpty()) {
            for (String name : queueOfCommands) {
                sb.append(name).append("\n");
            }
        } else {
            sb.append("History is empty");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        return new Response(sb.toString());
    }
}
