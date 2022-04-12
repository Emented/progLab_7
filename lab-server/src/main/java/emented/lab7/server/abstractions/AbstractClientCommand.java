package emented.lab7.server.abstractions;

import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;

public abstract class AbstractClientCommand {

    private final String name;
    private final int amountOfArgs;
    private final String description;
    private final String descriptionOfArgs;

    public AbstractClientCommand(String name, int amountOfArgs, String description, String descriptionOfArgs) {
        this.name = name;
        this.amountOfArgs = amountOfArgs;
        this.description = description;
        this.descriptionOfArgs = descriptionOfArgs;
    }

    public AbstractClientCommand(String name, int amountOfArgs, String description) {
        this.name = name;
        this.amountOfArgs = amountOfArgs;
        this.description = description;
        this.descriptionOfArgs = "";
    }

    public abstract Response executeClientCommand(Request request);

    public String getName() {
        return name;
    }

    public int getAmountOfArgs() {
        return amountOfArgs;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionOfArgs() {
        return descriptionOfArgs;
    }

    @Override
    public String toString() {
        return "Name of command: " + name + ", " + "args: "
                + ((amountOfArgs == 0) ? "this command doesn't need args" : descriptionOfArgs)
                + ", description: " + description;
    }
}
