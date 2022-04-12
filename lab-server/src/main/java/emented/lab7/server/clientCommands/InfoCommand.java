package emented.lab7.server.clientCommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.abstractions.AbstractClientCommand;
import emented.lab7.server.util.CollectionManager;

public class InfoCommand extends AbstractClientCommand {

    private final CollectionManager collectionInWork;

    public InfoCommand(CollectionManager collectionInWork) {
        super("info", 0, "display information about the collection");
        this.collectionInWork = collectionInWork;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return new Response(TextColoring.getGreenText("Info about collection:\n") + collectionInWork.returnInfo());
    }
}
