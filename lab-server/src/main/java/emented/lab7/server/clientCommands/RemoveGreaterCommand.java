package emented.lab7.server.clientCommands;

import emented.lab7.common.entities.MusicBand;
import emented.lab7.common.exceptions.CollectionIsEmptyException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CollectionManager;

import java.util.Set;

public class RemoveGreaterCommand extends AbstractClientCommand {

    private final CollectionManager collectionInWork;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater", 0, "remove all items from the collection that exceed the specified");
        this.collectionInWork = collectionManager;
    }

    @Override
    public Response executeClientCommand(Request request) {
        try {
            Set<MusicBand> res = collectionInWork.removeIfGreater(request.getBandArgument());
            if (res.isEmpty()) {
                return new Response(TextColoring.getGreenText("Not a single item has been deleted"));
            } else {
                return new Response(TextColoring.getGreenText("These items have been removed from the collection:"), res);
            }
        } catch (CollectionIsEmptyException e) {
            return new Response(TextColoring.getRedText(e.getMessage()));
        }
    }
}
