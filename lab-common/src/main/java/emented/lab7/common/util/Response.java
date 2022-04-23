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

    public List<Long> getListOfIds() {
        return listOfIds;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getInfoAboutResponse() {
        return "Response contains: " + (messageToResponse == null ? "" : "message")
                + (bandToResponse == null ? "" : ", musicband")
                + (collectionToResponse == null ? "" : ", collection");
    }

    @Override
    public String toString() {
        StringBuilder collection = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        if (collectionToResponse != null) {
            List<MusicBand> sortedBands = new ArrayList<>(collectionToResponse);
            sortedBands = sortedBands.stream().sorted(Comparator.comparing(SizeAnalyzer::getSizeOfBand).reversed()).collect(Collectors.toList());
            for (MusicBand m : sortedBands) {
                collection.append(m.toString()).append("\n");
            }
            collection = new StringBuilder(collection.substring(0, collection.length() - 1));
        }
        if (listOfIds != null) {
            for (Long id : listOfIds) {
                ids.append(id).append(", ");
            }
            ids = new StringBuilder(ids.substring(0, ids.length() - 2));
        }
        return (messageToResponse == null ? "" : messageToResponse)
                + (bandToResponse == null ? "" : "\n" + bandToResponse)
                + ((collectionToResponse == null) ? "" : "\n" + collection)
                + ((listOfIds == null) ? "" : "\n" + ids);
    }
}
