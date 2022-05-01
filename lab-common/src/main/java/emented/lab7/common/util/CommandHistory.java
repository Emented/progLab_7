package emented.lab7.common.util;

import java.util.ArrayDeque;

public class CommandHistory {
    private final ArrayDeque<String> history = new ArrayDeque<>(9);
    private final int dequeOverflow = 10;

    public ArrayDeque<String> getHistory() {
        return history;
    }

    public void pushCommand(String name) {
        history.addFirst(name);
        if (history.size() == dequeOverflow) {
            history.pollLast();
        }
    }
}
