package io.github.lucariatias.harmonicmoon.mapeditor.world;

public class Location {

    private String worldName;
    private int x;
    private int y;

    public Location(String worldName, int x, int y) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
    }

    public Location() {}

    public Location(Location original) {
        this.worldName = original.worldName;
        this.x = original.x;
        this.y = original.y;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
