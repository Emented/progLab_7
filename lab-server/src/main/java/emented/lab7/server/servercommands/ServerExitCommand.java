package emented.lab7.server.servercommands;

import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;
import emented.lab7.server.abstractions.AbstractServerCommand;

public class ServerExitCommand extends AbstractServerCommand {

    public ServerExitCommand() {
        super("exit", "shut downs the server");
    }

    @Override
    public String executeServerCommand() {
        ServerConfig.toggleRun();
        return TextColoring.getGreenText("Server shutdown");
    }
}
