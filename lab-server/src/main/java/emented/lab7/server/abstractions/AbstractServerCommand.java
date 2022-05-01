package emented.lab7.server.abstractions;

public abstract class AbstractServerCommand {

    private final String name;
    private final String description;

    public AbstractServerCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public abstract String executeServerCommand();

    @Override
    public String toString() {
        return "Name of command: " + name + ", description: " + description;
    }
}
