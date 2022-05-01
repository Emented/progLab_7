package emented.lab7.client.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ScriptReader {

    private ArrayList<CommandToSend> commandsFromFile = new ArrayList<>();

    public void readCommandsFromFile(String fileName) throws IOException {
        FileInputStream stream = new FileInputStream(fileName);
        commandsFromFile = streamToArrayOfCommands(stream);
    }

    private ArrayList<CommandToSend> streamToArrayOfCommands(FileInputStream file) throws IOException {
        ArrayList<CommandToSend> commands = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        String line = br.readLine();
        while (line != null) {
            String[] splitedInput = line.split(" ");
            String commandName = splitedInput[0].toLowerCase(Locale.ROOT);
            String[] commandsArgs = Arrays.copyOfRange(splitedInput, 1, splitedInput.length);
            commands.add(new CommandToSend(commandName, commandsArgs));
            line = br.readLine();
        }
        br.close();
        return commands;
    }

    public ArrayList<CommandToSend> getCommandsFromFile() {
        return commandsFromFile;
    }
}
