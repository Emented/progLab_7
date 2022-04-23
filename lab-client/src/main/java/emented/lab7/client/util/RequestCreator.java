package emented.lab7.client.util;

import emented.lab7.client.ClientConfig;
import emented.lab7.client.workWithCommandLine.MusicBandGenerator;
import emented.lab7.common.exceptions.WrongAmountOfArgsException;
import emented.lab7.common.exceptions.WrongArgException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.RequestType;
import emented.lab7.common.util.TextColoring;

public final class RequestCreator {

    private RequestCreator() {
    }

    public static Request createRequestOfCommand(CommandToSend command) {
        String name = command.getCommandName();
        Request request;
        if (AvailableCommands.COMMANDS_WITHOUT_ARGS.contains(name)) {
            request = createRequestWithoutArgs(command);
        } else if (AvailableCommands.COMMANDS_WITH_ID_ARG.contains(name)) {
            request = createRequestWithID(command);
        } else if (AvailableCommands.COMMANDS_WITH_NUMBER_OF_PARTICIPANTS_ARG.contains(name)) {
            request = createRequestWithNumOfParticipants(command);
        } else if (AvailableCommands.COMMANDS_WITH_BAND_ARG.contains(name)) {
            request = createRequestWithBand(command);
        } else if (AvailableCommands.COMMANDS_WITH_BAND_ID_ARGS.contains(name)) {
            request = createRequestWithBandID(command);
        } else {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("There is no such command, type HELP to get list on commands"));
            request = null;
        }

        return request;
    }

    private static Request createRequestWithoutArgs(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            return new Request(command.getCommandName(), RequestType.COMMAND);
        } catch (WrongAmountOfArgsException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            return null;
        }
    }

    private static Request createRequestWithID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long id = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "ID must be greater then 0",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            return new Request(command.getCommandName(), id, RequestType.COMMAND);
        } catch (WrongAmountOfArgsException | WrongArgException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            return null;
        } catch (IllegalArgumentException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Wrong data type of argument"));
            return null;
        }
    }

    private static Request createRequestWithNumOfParticipants(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long numberOfParticipants = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "Number of participants must be greater then 0",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            return new Request(command.getCommandName(), numberOfParticipants, RequestType.COMMAND);
        } catch (WrongAmountOfArgsException | WrongArgException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            return null;
        } catch (IllegalArgumentException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Wrong data type of argument"));
            return null;
        }
    }

    private static Request createRequestWithBand(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            MusicBandGenerator generator = new MusicBandGenerator();
            generator.setVariables();
            return new Request(command.getCommandName(), generator.getGeneratedMusicBand(), RequestType.COMMAND);
        } catch (WrongAmountOfArgsException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            return null;
        }
    }

    private static Request createRequestWithBandID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            long id = CommandValidators.validateArg(arg -> ((long) arg) > 0,
                    "ID must be greater then 0",
                    Long::parseLong,
                    command.getCommandArgs()[0]);
            MusicBandGenerator generator = new MusicBandGenerator();
            generator.setVariables();
            return new Request(command.getCommandName(), id, generator.getGeneratedMusicBand(), RequestType.COMMAND);
        } catch (WrongAmountOfArgsException | WrongArgException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            return null;
        } catch (IllegalArgumentException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Wrong data type of argument"));
            return null;
        }
    }
}
