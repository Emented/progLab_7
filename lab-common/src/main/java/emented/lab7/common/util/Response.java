package emented.lab7.common.util;

import emented.lab7.common.entities.MusicBand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Response implements Serializable {

    private String messageToResponse;
    private MusicBand bandToResponse;
    private Set<MusicBand> collectionToResponse;

    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    public Response(String messageToResponse, MusicBand bandToResponse) {
        this.messageToResponse = messageToResponse;
        this.bandToResponse = bandToResponse;
    }

    public Response(String messageToResponse, Set<MusicBand> collectionToResponse) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
    }

    public Response(MusicBand bandToResponse) {
        this.bandToResponse = bandToResponse;
    }

    public Response(Set<MusicBand> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }

    public String getMessageToResponse() {
        return messageToResponse;
    }

    public MusicBand getBandToResponse() {
        return bandToResponse;
    }

    public Set<MusicBand> getCollectionToResponse() {
        return collectionToResponse;
    }

    public String getInfoAboutResponse() {
        return "Response contains: " + (messageToResponse == null ? "" : "message")
                + (bandToResponse == null ? "" : ", musicband")
                + (collectionToResponse == null ? "" : ", collection");
    }

    @Override
    public String toString() {
        StringBuilder collection = new StringBuilder();
        if (collectionToResponse != null) {
            List<MusicBand> sortedBands = new ArrayList<>(collectionToResponse);
            sortedBands = sortedBands.stream().sorted(Comparator.comparing(SizeAnalyzer::getSizeOfBand).reversed()).collect(Collectors.toList());
            for (MusicBand m : sortedBands) {
                collection.append(m.toString()).append("\n");
            }
            collection = new StringBuilder(collection.substring(0, collection.length() - 1));
        }
        return (messageToResponse == null ? "" : messageToResponse)
                + (bandToResponse == null ? "" : "\n" + bandToResponse)
                + ((collectionToResponse == null) ? "" : "\n"
                + collection);
    }
}
