package de.eleon.asciifx.data;

public class AutosaveEvent {

    public enum Type{ START, STOP}

    private final Type type;

    public AutosaveEvent(Type type) {
        this.type = type;
    }

    public Type type() {
        return type;
    }

}
