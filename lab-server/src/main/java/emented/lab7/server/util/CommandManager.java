package emented.lab7.server.util;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.abstractions.AbstractServerCommand;

import java.time.format.DateTimeFormatter;

public class CommandManager {

    public CommandManager(AbstractClientCommand helpClientCommand,
                          AbstractClientCommand infoCommand,
                          AbstractClientCommand showCommand,
                          AbstractClientCommand addCommand,
                          AbstractClientCommand updateCommand,
                          AbstractClientCommand removeByIdCommand,
                          AbstractClientCommand clearCommand,
                          AbstractClientCommand exitClientCommand,
                          AbstractClientCommand addIfMaxCommand,
                          AbstractClientCommand removeGreaterCommand,
                          AbstractClientCommand historyCommand,
                          AbstractClientCommand removeAnyByNumberOfParticipantsCommand,
                          AbstractClientCommand minByStudioCommand,
                          AbstractClientCommand countLessThanNumberOfParticipantsCommand,
                          AbstractClientCommand executeScriptCommand,
                          AbstractServerCommand helpServerCommand,
                          AbstractServerCommand exitServerCommand,
                          AbstractServerCommand historyServerCommand) {

        ServerConfig.getClientAvailableCommands().put(helpClientCommand.getName(), helpClientCommand);
        ServerConfig.getClientAvailableCommands().put(infoCommand.getName(), infoCommand);
        ServerConfig.getClientAvailableCommands().put(showCommand.getName(), showCommand);
        ServerConfig.getClientAvailableCommands().put(addCommand.getName(), addCommand);
        ServerConfig.getClientAvailableCommands().put(updateCommand.getName(), updateCommand);
        ServerConfig.getClientAvailableCommands().put(removeByIdCommand.getName(), removeByIdCommand);
        ServerConfig.getClientAvailableCommands().put(clearCommand.getName(), clearCommand);
        ServerConfig.getClientAvailableCommands().put(exitClientCommand.getName(), exitClientCommand);
        ServerConfig.getClientAvailableCommands().put(addIfMaxCommand.getName(), addIfMaxCommand);
        ServerConfig.getClientAvailableCommands().put(removeGreaterCommand.getName(), removeGreaterCommand);
        ServerConfig.getClientAvailableCommands().put(historyCommand.getName(), historyCommand);
        ServerConfig.getClientAvailableCommands().put(removeAnyByNumberOfParticipantsCommand.getName(), removeAnyByNumberOfParticipantsCommand);
        ServerConfig.getClientAvailableCommands().put(minByStudioCommand.getName(), minByStudioCommand);
        ServerConfig.getClientAvailableCommands().put(countLessThanNumberOfParticipantsCommand.getName(), countLessThanNumberOfParticipantsCommand);
        ServerConfig.getClientAvailableCommands().put(executeScriptCommand.getName(), executeScriptCommand);

        ServerConfig.getServerAvailableCommands().put(helpServerCommand.getName(), helpServerCommand);
        ServerConfig.getServerAvailableCommands().put(exitServerCommand.getName(), exitServerCommand);
        ServerConfig.getServerAvailableCommands().put(historyServerCommand.getName(), historyServerCommand);
    }

    public Response executeClientCommand(Request request) {
        ServerConfig.getClientCommandHistory().pushCommand(TextColoring.getBlueText(request.getCurrentTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                + " " + TextColoring.getGreenText(request.getClientInfo()) + ": " + request.getCommandName());
        return ServerConfig.getClientAvailableCommands().get(request.getCommandName()).executeClientCommand(request);
    }

    public String executeServerCommand(String commandName) {
        if (ServerConfig.getServerAvailableCommands().containsKey(commandName)) {
            return ServerConfig.getServerAvailableCommands().get(commandName).executeServerCommand();
        } else {
            return TextColoring.getRedText("There is no such command, type HELP to get list on commands");
        }
    }
}
