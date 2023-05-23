package de.hthoene.playerstatslib;

public class PlayerEntry {
    private final String uuid;
    private final int value;

    public PlayerEntry(String uuid, int value) {
        this.uuid = uuid;
        this.value = value;
    }

    public String getUuid() {
        return uuid;
    }

    public int getValue() {
        return value;
    }
}