package emented.lab7.server.clientcommands;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import emented.lab7.server.abstractions.AbstractClientCommand;

public class ExecuteScriptCommand extends AbstractClientCommand {

    public ExecuteScriptCommand() {
        super("execute_script",
                1,
                "read and execute the script from the specified file",
                "file name");
    }

    @Override
    public Response executeClientCommand(Request request) {
        return null;
    }
}
