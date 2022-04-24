package emented.lab7.server.util;

import emented.lab7.common.entities.MusicBand;
import emented.lab7.common.exceptions.CollectionIsEmptyException;
import emented.lab7.common.exceptions.DatabaseException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.db.DBManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandProcessor {

    private final DBManager dbManager;
    private final CollectionManager collectionManager;


    public CommandProcessor(DBManager dbManager, CollectionManager collectionManager) {
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    public Response add(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                MusicBand bandToAdd = request.getBandArgument();
                Long id = dbManager.addElement(bandToAdd, request.getUsername());
                bandToAdd.setId(id);
                collectionManager.addMusicBand(bandToAdd);
                return new Response(TextColoring.getGreenText("Element was successfully added to collection with ID: "
                        + id));
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response removeById(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                if (dbManager.checkBandExistence(request.getNumericArgument())) {
                    if (dbManager.removeById(request.getNumericArgument(), request.getUsername())) {
                        collectionManager.removeBandById(request.getNumericArgument());
                        return new Response(TextColoring.getGreenText("Element with ID " + request.getNumericArgument()
                                + " was removed from the collection"));
                    } else {
                        return new Response(TextColoring.getRedText("Element was created by another user, you don't "
                                + "have permission to remove it"));
                    }
                } else {
                    return new Response(TextColoring.getRedText("There is no element with such ID"));
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response updateById(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                if (dbManager.checkBandExistence(request.getNumericArgument())) {
                    if (dbManager.updateById(request.getBandArgument(), request.getNumericArgument(), request.getUsername())) {
                        collectionManager.updateById(request.getNumericArgument(), request.getBandArgument());
                        return new Response(TextColoring.getGreenText("Element with ID " + request.getNumericArgument()
                                + " was successfully updated"));
                    } else {
                        return new Response(TextColoring.getRedText("Element was created by another user, you don't "
                                + "have permission to update it"));
                    }
                } else {
                    return new Response(TextColoring.getRedText("There is no element with such ID"));
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response clear(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                List<Long> deletedIDs = dbManager.clear(request.getUsername());
                if (deletedIDs.isEmpty()) {
                    return new Response(TextColoring.getRedText("You don't have elements in this collection!"));
                } else {
                    deletedIDs.forEach(collectionManager::removeBandById);
                    return new Response(TextColoring.getGreenText("Your elements were removed from the collection, their IDs:"), deletedIDs);
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response removeGreater(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                List<Long> ids = collectionManager.returnIDsOfGreater(request.getBandArgument());
                if (ids.isEmpty()) {
                    return new Response(TextColoring.getRedText("There are no such elements in collection"));
                } else {
                    List<Long> removedIDs = new ArrayList<>();
                    for (Long id : ids) {
                        if (dbManager.removeById(id, request.getUsername())) {
                            removedIDs.add(id);
                            collectionManager.removeBandById(id);
                        }
                    }
                    if (removedIDs.isEmpty()) {
                        return new Response(TextColoring.getRedText("There are no such elements, that belong to you in collection"));
                    } else {
                        return new Response(TextColoring.getGreenText("Elements with this IDs were removed from the collection"), removedIDs);
                    }
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response show(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                if (collectionManager.getMusicBands().isEmpty()) {
                    return new Response(TextColoring.getGreenText("Collection is empty"));
                } else {
                    List<Long> ids = dbManager.getIdsOfUsersElements(request.getUsername());
                    return new Response(TextColoring.getGreenText("Elements of collection:"),
                            collectionManager.getUsersElements(ids),
                            collectionManager.getAlienElements(ids));
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response info(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                return new Response(TextColoring.getGreenText("Info about collection:\n") + collectionManager.returnInfo());
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response countLessThenNumberOfParticipants(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                int result = collectionManager.countLessThanNumberOfParticipants(request.getNumericArgument());
                return new Response("Groups with fewer participants than "
                        + request.getNumericArgument()
                        + ": " + result);
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response removeAnyByNumberOfParticipants(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                List<Long> ids = collectionManager.returnIDbyNumberOFParticipants(request.getNumericArgument());
                if (ids.isEmpty()) {
                    return new Response(TextColoring.getRedText("There is no band with number of participants equals "
                            + request.getNumericArgument()));
                } else {
                    for (Long id : ids) {
                        if (dbManager.removeById(id, request.getUsername())) {
                            collectionManager.removeBandById(id);
                            return new Response(TextColoring.getGreenText("MusicBand with " + request.getNumericArgument()
                                    + " participants and ID equals "
                                    + id + " was removed"));
                        }
                    }
                    return new Response(TextColoring.getRedText("There are no such elements, that belong to you "
                            + "in this collection with " + request.getNumericArgument() + " participants"));
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response addIfMax(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                MusicBand bandToAdd = request.getBandArgument();
                if (collectionManager.checkMax(bandToAdd)) {
                    Long id = dbManager.addElement(bandToAdd, request.getUsername());
                    bandToAdd.setId(id);
                    collectionManager.addMusicBand(bandToAdd);
                    return new Response(TextColoring.getGreenText("Element was successfully added to collection with ID: "
                            + id));
                } else {
                    return new Response(TextColoring.getRedText("Element is not max"));
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response minByStudio(Request request) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                try {
                    return new Response(TextColoring.getGreenText("Minimal element:"), collectionManager.returnMinByStudio());
                } catch (CollectionIsEmptyException e) {
                    return new Response(TextColoring.getRedText(e.getMessage()));
                }
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response help(Request request, HashMap<String, AbstractClientCommand> availableCommands) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                StringBuilder sb = new StringBuilder();
                for (AbstractClientCommand command : availableCommands.values()) {
                    sb.append(command.toString()).append("\n");
                }
                sb = new StringBuilder(sb.substring(0, sb.length() - 1));
                return new Response(TextColoring.getGreenText("Available commands:\n") + sb);
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }

    public Response history(Request request, ArrayDeque<String> queueOfCommands) {
        try {
            if (dbManager.validateUser(request.getUsername(), request.getPassword())) {
                StringBuilder sb = new StringBuilder();
                if (!queueOfCommands.isEmpty()) {
                    for (String name : queueOfCommands) {
                        sb.append(name).append("\n");
                    }
                } else {
                    sb.append("History is empty");
                }
                sb = new StringBuilder(sb.substring(0, sb.length() - 1));
                return new Response(sb.toString());
            } else {
                return new Response(TextColoring.getRedText("Login and password mismatch"));
            }
        } catch (DatabaseException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }
}
