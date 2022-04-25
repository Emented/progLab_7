package emented.lab7.client.util;

import emented.lab7.client.ClientConfig;
import emented.lab7.client.workWithCommandLine.ClientCommandListener;
import emented.lab7.common.exceptions.WrongAmountOfArgsException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

public class Session {

    private final List<String> userInfo;
    private final ClientSocketWorker clientSocketWorker;
    private final ClientCommandListener commandListener;
    private boolean statusOfCommandListening = true;

    public Session(List<String> userInfo, ClientSocketWorker clientSocketWorker) {
        this.userInfo = userInfo;
        this.clientSocketWorker = clientSocketWorker;
        commandListener = new ClientCommandListener(System.in, userInfo.get(0));
    }

    public void startSession() {
        while (statusOfCommandListening) {
            CommandToSend command = commandListener.readCommand();
            if (command != null) {
                if ("exit".equals(command.getCommandName().toLowerCase(Locale.ROOT))) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Client shutdown"));
                    toggleStatus();
                } else if (AvailableCommands.SCRIPT_ARGUMENT_COMMAND.equals(command.getCommandName())) {
                    executeScript(command.getCommandArgs());
                } else {
                    if (sendRequest(command)) {
                        receiveResponse();
                    }
                }
            }
        }
    }


    private boolean sendRequest(CommandToSend command) {
        Request request = RequestCreator.createRequestOfCommand(command);
        if (request != null) {
            request.setCurrentTime(LocalTime.now());
            request.setClientInfo(clientSocketWorker.getAddress() + " " + clientSocketWorker.getPort());
            request.setUsername(userInfo.get(0));
            request.setPassword(userInfo.get(1));
            try {
                clientSocketWorker.sendRequest(request);
                return true;
            } catch (IOException e) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while serializing the request, try again"));
                return false;
            }
        } else {
            return false;
        }
    }

    private void receiveResponse() {
        try {
            Response response = clientSocketWorker.receiveResponse();
            ClientConfig.getConsoleTextPrinter().printlnText(response.toString());
        } catch (SocketTimeoutException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The waiting time for a response from the server has been exceeded, try again later"));
        } catch (IOException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while receiving a response from the server"));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("The response came damaged"));
        }
    }

    private void executeScript(String[] args) {
        try {
            CommandValidators.validateAmountOfArgs(args, 1);
            ScriptReader reader = new ScriptReader();

            if (ClientConfig.getHistoryOfScripts().contains(args[0])) {
                ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Possible looping, change your script"));
            } else {
                reader.readCommandsFromFile(args[0]);
                ClientConfig.getHistoryOfScripts().add(args[0]);
                ArrayList<CommandToSend> commands = reader.getCommandsFromFile();
                for (CommandToSend command : commands) {
                    ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getBlueText("Executing... " + command.getCommandName()));
                    if ("execute_script".equals(command.getCommandName())) {
                        executeScript(command.getCommandArgs());
                    } else {
                        if (sendRequest(command)) {
                            receiveResponse();
                            ClientConfig.getHistoryOfScripts().remove(command.getCommandName());
                        }
                    }
                }
            }
        } catch (WrongAmountOfArgsException | IOException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
        } catch (NoSuchElementException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
            System.exit(1);
        }
    }

    public void toggleStatus() {
        statusOfCommandListening = !statusOfCommandListening;
    }
}
