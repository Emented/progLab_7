package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;

import java.util.HashMap;

public class HelpCommand extends AbstractClientCommand {

    private final HashMap<String, AbstractClientCommand> availableCommands;

    public HelpCommand(HashMap<String, AbstractClientCommand> availableCommands) {
        super("help", 0, "show list available commands");
        this.availableCommands = availableCommands;
    }

    @Override
    public Response executeClientCommand(Request request) {
        StringBuilder sb = new StringBuilder();
        for (AbstractClientCommand command : availableCommands.values()) {
            sb.append(command.toString()).append("\n");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        return new Response(TextColoring.getGreenText("Available commands:\n") + sb);
    }
}
