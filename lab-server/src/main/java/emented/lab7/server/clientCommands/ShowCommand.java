package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CollectionManager;

public class ShowCommand extends AbstractClientCommand {

    private final CollectionManager collectionInWork;

    public ShowCommand(CollectionManager collectionInWork) {
        super("show", 0, "display all the elements of the collection and information about them");
        this.collectionInWork = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        if (collectionInWork.getMusicBands().isEmpty()) {
            return new Response(TextColoring.getGreenText("Collection is empty"));
        } else {
            return new Response(TextColoring.getGreenText("Elements of collection:"), collectionInWork.getMusicBands());
        }
    }
}
