package io.github.lucariatias.hmmapedit;

import java.awt.*;

public class WorldObject {

    private int r;
    private int g;
    private int b;
    private Location location;

    public WorldObject(int red, int green, int blue, Location location) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.location = location;
    }

    public WorldObject(Color colour, Location location) {
        this(colour.getRed(), colour.getGreen(), colour.getBlue(), location);
    }

    public Location getLocation() {
        return location;
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.drawRect(location.getX(), location.getY(), 16, 16);
        graphics.setFont(graphics.getFont().deriveFont(8F));
        graphics.drawString(r + "," + g + "," + b, location.getX(), location.getY() + 16);
    }

}
