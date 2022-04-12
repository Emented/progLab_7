package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CollectionManager;

public class AddCommand extends AbstractClientCommand {

    private final CollectionManager collectionInWork;

    public AddCommand(CollectionManager collectionInWork) {
        super("add", 0, "add a new item to the collection");
        this.collectionInWork = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        collectionInWork.addMusicBand(request.getBandArgument());
        return new Response(TextColoring.getGreenText("New element was successfully added!"), request.getBandArgument());
    }
}
