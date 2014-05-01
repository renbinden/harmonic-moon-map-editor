package io.github.lucariatias.hmmapedit;

public class Location {

    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Location getRelative(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }

    public int distanceSquared(Location location) {
        int xDiff = location.getX() - x;
        int yDiff = location.getY() - y;
        return (xDiff * xDiff) + (yDiff * yDiff);
    }

    public int distance(Location location) {
        return (int) Math.round(Math.sqrt(distanceSquared(location)));
    }

}
