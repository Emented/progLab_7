package emented.lab7.server.clientCommands;

import emented.lab7.common.exceptions.IDNotFoundException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CollectionManager;

public class RemoveByIdCommand extends AbstractClientCommand {

    private final CollectionManager collectionInWork;

    public RemoveByIdCommand(CollectionManager collectionInWork) {
        super("remove_by_id", 1, "delete a group from a collection by its id", "id");
        this.collectionInWork = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            collectionInWork.removeBandById(request.getNumericArgument());
            return new Response(TextColoring.getGreenText("Group with ID " + request.getNumericArgument() + " was deleted from collection"));
        } catch (IDNotFoundException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }
}
