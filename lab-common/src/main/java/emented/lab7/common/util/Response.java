package emented.lab7.common.util;

import emented.lab7.common.entities.MusicBand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Response implements Serializable {

    private final String messageToResponse;
    private MusicBand bandToResponse;
    private Set<MusicBand> yourElementsOfCollection;
    private Set<MusicBand> alienElementsOfCollection;
    private boolean success = true;
    private List<Long> listOfIds;

    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    public Response(String messageToResponse, MusicBand bandToResponse) {
        this.messageToResponse = messageToResponse;
        this.bandToResponse = bandToResponse;
    }

    public Response(String messageToResponse, List<Long> listOfIds) {
        this.messageToResponse = messageToResponse;
        this.listOfIds = listOfIds;
    }

    public Response(String messageToResponse, boolean success) {
        this.messageToResponse = messageToResponse;
        this.success = success;
    }

    public Response(String messageToResponse, Set<MusicBand> yourElementsOfCollection, Set<MusicBand> alienElementsOfCollection) {
        this.messageToResponse = messageToResponse;
        this.yourElementsOfCollection = yourElementsOfCollection;
        this.alienElementsOfCollection = alienElementsOfCollection;
    }

    public String getMessageToResponse() {
        return messageToResponse;
    }

    public MusicBand getBandToResponse() {
        return bandToResponse;
    }

    public Set<MusicBand> getYourElementsOfCollection() {
        return yourElementsOfCollection;
    }

    public Set<MusicBand> getAlienElementsOfCollection() {
        return alienElementsOfCollection;
    }

    public List<Long> getListOfIds() {
        return listOfIds;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getInfoAboutResponse() {
        return "Response contains: " + (messageToResponse == null ? "" : "message")
                + (bandToResponse == null ? "" : ", musicband")
                + (yourElementsOfCollection == null ? "" : ", collection");
    }

    @Override
    public String toString() {
        StringBuilder collection = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        if (yourElementsOfCollection != null) {
            List<MusicBand> sortedBands = new ArrayList<>(yourElementsOfCollection);
            sortedBands = sortedBands.stream().sorted(Comparator.comparing(SizeAnalyzer::getSizeOfBand).reversed()).collect(Collectors.toList());
            collection.append(TextColoring.getGreenText("Your elements:\n"));
            for (MusicBand m : sortedBands) {
                collection.append(m.toString()).append("\n");
            }
        } else {
            collection.append(TextColoring.getGreenText("You don't have elements in this collection!\n"));
        }
        if (alienElementsOfCollection != null) {
            List<MusicBand> sortedBands = new ArrayList<>(alienElementsOfCollection);
            sortedBands = sortedBands.stream().sorted(Comparator.comparing(SizeAnalyzer::getSizeOfBand).reversed()).collect(Collectors.toList());
            collection.append(TextColoring.getGreenText("Another user's elements:\n"));
            for (MusicBand m : sortedBands) {
                collection.append(m.toString()).append("\n");
            }
            collection = new StringBuilder(collection.substring(0, collection.length() - 1));
        }  else {
            collection.append(TextColoring.getGreenText("Another users don't have elements in this collection!"));
        }
        if (listOfIds != null) {
            for (Long id : listOfIds) {
                ids.append(id).append(", ");
            }
            ids = new StringBuilder(ids.substring(0, ids.length() - 2));
        }
        return (messageToResponse == null ? "" : messageToResponse)
                + (bandToResponse == null ? "" : "\n" + bandToResponse)
                + ((yourElementsOfCollection == null && alienElementsOfCollection == null) ? "" : "\n" + collection)
                + ((listOfIds == null) ? "" : "\n" + ids);
    }
}
