package emented.lab7.common.util;

import emented.lab7.common.entities.MusicBand;

import java.io.Serializable;
import java.time.LocalTime;

public class Request implements Serializable {

    private final String commandName;
    private String clientInfo;
    private LocalTime currentTime;
    private Long numericArgument;
    private MusicBand bandArgument;

    public Request(String commandName) {
        this.commandName = commandName;
    }

    public Request(String commandName, Long numericArgument) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
    }

    public Request(String commandName, MusicBand bandArgument) {
        this.commandName = commandName;
        this.bandArgument = bandArgument;
    }

    public Request(String commandName, Long numericArgument, MusicBand bandArgument) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.bandArgument = bandArgument;
    }

    public String getCommandName() {
        return commandName;
    }

    public Long getNumericArgument() {
        return numericArgument;
    }

    public MusicBand getBandArgument() {
        return bandArgument;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "Name of command to send: " + commandName
                + (bandArgument == null ? "" : "\nInfo about band to send: " + bandArgument)
                + (numericArgument == null ? "" : "\nNumeric argument to send: " + numericArgument);
    }
}
