package emented.lab7.client.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AvailableCommands {

    public static final Set<String> COMMANDS_WITHOUT_ARGS = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_ID_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_NUMBER_OF_PARTICIPANTS_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_BAND_ARG = new HashSet<>();
    public static final Set<String> COMMANDS_WITH_BAND_ID_ARGS = new HashSet<>();
    public static final String SCRIPT_ARGUMENT_COMMAND;

    static {
        Collections.addAll(COMMANDS_WITHOUT_ARGS,
                "help",
                "show",
                "info",
                "history",
                "min_by_studio",
                "clear"
        );
        Collections.addAll(COMMANDS_WITH_ID_ARG,
                "remove_by_id"
        );
        Collections.addAll(COMMANDS_WITH_NUMBER_OF_PARTICIPANTS_ARG,
                "count_less_than_number_of_participants",
                "remove_any_by_number_of_participants"
        );
        Collections.addAll(COMMANDS_WITH_BAND_ARG,
                "add",
                "add_if_max",
                "remove_greater"
        );
        Collections.addAll(COMMANDS_WITH_BAND_ID_ARGS,
                "update");
        SCRIPT_ARGUMENT_COMMAND = "execute_script";
    }

    private AvailableCommands() {
    }

}
