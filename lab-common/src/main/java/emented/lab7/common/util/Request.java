package emented.lab7.common.util;

import emented.lab7.common.entities.MusicBand;

import java.io.Serializable;
import java.time.LocalTime;

public class Request implements Serializable {

    private String commandName;
    private String clientInfo;
    private LocalTime currentTime;
    private Long numericArgument;
    private MusicBand bandArgument;
    private String username;
    private String password;
    private final RequestType requestType;

    public Request(String username, String password, RequestType requestType) {
        this.username = username;
        this.password = password;
        this.requestType = requestType;
    }

    public Request(String commandName, RequestType requestType) {
        this.commandName = commandName;
        this.requestType = requestType;
    }

    public Request(String commandName, Long numericArgument, RequestType requestType) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.requestType = requestType;
    }

    public Request(String commandName, MusicBand bandArgument, RequestType requestType) {
        this.commandName = commandName;
        this.bandArgument = bandArgument;
        this.requestType = requestType;
    }

    public Request(String commandName, Long numericArgument, MusicBand bandArgument, RequestType requestType) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.bandArgument = bandArgument;
        this.requestType = requestType;
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

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    public String toString() {
        if (requestType.equals(RequestType.REGISTER)) {
            return "Register request";
        } else if (requestType.equals(RequestType.LOGIN)) {
            return "Login request";
        } else {
            return "Name of command to send: " + commandName
                    + (bandArgument == null ? "" : "\nInfo about band to send: " + bandArgument)
                    + (numericArgument == null ? "" : "\nNumeric argument to send: " + numericArgument);
        }
    }
}
