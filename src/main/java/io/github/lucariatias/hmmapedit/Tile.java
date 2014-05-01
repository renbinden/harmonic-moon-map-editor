package io.github.lucariatias.hmmapedit;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Tile {

    private boolean selected;
    private Camera camera;
    private BufferedImage image;
    private Map<Layer, Set<Location>> locations = new EnumMap<>(Layer.class);

    private int tileSheetX;
    private int tileSheetY;

    public Tile(Camera camera, BufferedImage image, int tileSheetX, int tileSheetY) {
        this.camera = camera;
        this.image = image;
        this.tileSheetX = tileSheetX;
        this.tileSheetY = tileSheetY;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void addLocation(Layer layer, Location location) {
        getLocations(layer).add(location);
    }

    public Set<Location> getLocations(Layer layer) {
        if (!locations.containsKey(layer)) locations.put(layer, new HashSet<Location>());
        return locations.get(layer);
    }

    public void render(Graphics graphics, Layer layer) {
        if (!locations.containsKey(layer)) return;
        for (Location location : locations.get(layer)) {
            if (location.distanceSquared(camera.getLocation()) >= 640000) continue;
            graphics.drawImage(image, location.getX(), location.getY(), null);
        }
    }

    public int getTileSheetX() {
        return tileSheetX;
    }

    public int getTileSheetY() {
        return tileSheetY;
    }

    public Color toMapColour() {
        return new Color(tileSheetX, tileSheetY, 0);
    }

}
