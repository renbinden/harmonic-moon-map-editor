package io.github.lucariatias.hmmapedit;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.*;

public class MapPanel extends JPanel {

    private MapFrame mapFrame;

    private Camera camera;

    private BufferedImage backTileMap;
    private BufferedImage backTopTileMap;
    private BufferedImage objectMap;
    private BufferedImage frontTileMap;
    private TileSheet tileSheet;
    private Map<Layer, Set<Tile>> tiles = new EnumMap<>(Layer.class);
    private Set<WorldObject> objects = new HashSet<>();

    public MapPanel(MapFrame mapFrame, Camera camera, BufferedImage backTileMap, BufferedImage backTopTileMap, BufferedImage objectMap, BufferedImage frontTileMap, TileSheet tileSheet) {
        setLayout(null);
        setBackground(Color.BLACK);
        this.mapFrame = mapFrame;
        this.camera = camera;
        this.backTileMap = backTileMap;
        this.backTopTileMap = backTopTileMap;
        this.objectMap = objectMap;
        this.frontTileMap = frontTileMap;
        this.tileSheet = tileSheet;
        mapFrame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                MapPanel.this.handleMouse(event);
            }
        });
        mapFrame.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                MapPanel.this.handleMouse(event);
            }
        });
    }

    private void handleMouse(MouseEvent event) {
        /*if (SwingUtilities.isMiddleMouseButton(event)) {
            MapPanel.this.mapFrame.setScrollingEnabled(!MapPanel.this.mapFrame.isScrollingEnabled());
            return;
        }*/
        int mouseWindowX = (int) (event.getXOnScreen() - getLocationOnScreen().getX());
        int mouseWindowY = (int) (event.getYOnScreen() - getLocationOnScreen().getY());
        int mouseMapX = mouseWindowX + camera.getLocation().getX();
        int mouseMapY = mouseWindowY + camera.getLocation().getY();
        int mapTileX = mouseMapX / 16;
        int mapTileY = mouseMapY / 16;
        int tileX = 0, tileY = 0;
        for (Tile tile : tileSheet.getTiles()) {
            if (tile.isSelected()) {
                tileX = tile.getTileSheetX();
                tileY = tile.getTileSheetY();
                if (!(tileX == 0 && tileY == 0) || mapFrame.getTileFrame().getSelectedLayer() == Layer.BACK) {
                    getTiles(mapFrame.getTileFrame().getSelectedLayer()).add(tile);
                    tile.addLocation(mapFrame.getTileFrame().getSelectedLayer(), new Location(mapTileX * 16, mapTileY * 16));
                }
            } else {
                Iterator<Location> iterator = tile.getLocations(mapFrame.getTileFrame().getSelectedLayer()).iterator();
                while (iterator.hasNext()) {
                    Location location = iterator.next();
                    if (location.getX() == mapTileX * 16 && location.getY() == mapTileY * 16) {
                        iterator.remove();
                    }
                }
            }
        }
        Iterator<WorldObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            WorldObject object = iterator.next();
            if (object.getLocation().getX() == mapTileX * 16 && object.getLocation().getY() == mapTileY * 16) {
                iterator.remove();
            }
        }
        if (mapFrame.getTileFrame().getSelectedObject() != ObjectType.NOTHING) objects.add(new WorldObject(mapFrame.getTileFrame().getSelectedObject().toColour(), new Location(mapTileX * 16, mapTileY * 16)));
        int r = tileX;
        int g = tileY;
        int b = 0;
        int col = (r << 16) | (g << 8) | b;
        switch (mapFrame.getTileFrame().getSelectedLayer()) {
            case BACK: backTileMap.setRGB(mapTileX, mapTileY, col); break;
            case BACK_TOP: backTopTileMap.setRGB(mapTileX, mapTileY, col); break;
            case FRONT: frontTileMap.setRGB(mapTileX, mapTileY, col); break;
        }
        objectMap.setRGB(mapTileX, mapTileY, mapFrame.getTileFrame().getSelectedObject().toColour().getRed() << 16 | mapFrame.getTileFrame().getSelectedObject().toColour().getGreen() << 8 | mapFrame.getTileFrame().getSelectedObject().toColour().getBlue());
        repaint();
    }

    public void populate() {
        if (backTileMap == null || backTopTileMap == null || objectMap == null || frontTileMap == null || tileSheet == null) return;
        for (int x = 0; x < backTileMap.getWidth(); x++) {
            for (int y = 0; y < backTileMap.getHeight(); y++) {
                int pixel = backTileMap.getRGB(x, y);
                Color colour = new Color((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
                Tile tile = tileSheet.getTile(colour.getRed(), colour.getGreen());
                tile.addLocation(Layer.BACK, new Location(x * 16, y * 16));
                getTiles(Layer.BACK).add(tile);
            }
        }
        for (int x = 0; x < backTopTileMap.getWidth(); x++) {
            for (int y = 0; y < backTopTileMap.getHeight(); y++) {
                int pixel = backTopTileMap.getRGB(x, y);
                Color colour = new Color((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
                if (!colour.equals(Color.BLACK)) {
                    Tile tile = tileSheet.getTile(colour.getRed(), colour.getGreen());
                    tile.addLocation(Layer.BACK_TOP, new Location(x * 16, y * 16));
                    getTiles(Layer.BACK_TOP).add(tile);
                }
            }
        }
        for (int x = 0; x < objectMap.getWidth(); x++) {
            for (int y = 0; y < objectMap.getHeight(); y++) {
                int pixel = objectMap.getRGB(x, y);
                Color colour = new Color((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
                if (!colour.equals(Color.BLACK)) {
                    WorldObject object = new WorldObject(colour, new Location(x * 16, y * 16));
                    objects.add(object);
                }
            }
        }
        for (int x = 0; x < frontTileMap.getWidth(); x++) {
            for (int y = 0; y < frontTileMap.getHeight(); y++) {
                int pixel = frontTileMap.getRGB(x, y);
                Color colour = new Color((pixel >> 16) & 0xff, (pixel >> 8) & 0xff, pixel & 0xff);
                if (!colour.equals(Color.BLACK)) {
                    Tile tile = tileSheet.getTile(colour.getRed(), colour.getGreen());
                    tile.addLocation(Layer.FRONT, new Location(x * 16, y * 16));
                    getTiles(Layer.FRONT).add(tile);
                }
            }
        }
    }

    public BufferedImage getBackTileMap() {
        return backTileMap;
    }

    public void setBackTileMap(BufferedImage backTileMap) {
        this.backTileMap = backTileMap;
        populate();
    }

    public BufferedImage getBackTopTileMap() {
        return backTopTileMap;
    }

    public void setBackTopTileMap(BufferedImage backTopTileMap) {
        this.backTopTileMap = backTopTileMap;
        populate();
    }

    public BufferedImage getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(BufferedImage objectMap) {
        this.objectMap = objectMap;
        populate();
    }

    public BufferedImage getFrontTileMap() {
        return frontTileMap;
    }

    public void setFrontTileMap(BufferedImage frontTileMap) {
        this.frontTileMap = frontTileMap;
        populate();
    }

    public TileSheet getTileSheet() {
        return tileSheet;
    }

    public void setTileSheet(TileSheet tileSheet) {
        this.tileSheet = tileSheet;
        populate();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.translate(-camera.getLocation().getX(), -camera.getLocation().getY());
        for (Tile tile : getTiles(Layer.BACK)) {
            tile.render(graphics, Layer.BACK);
        }
        for (Tile tile : getTiles(Layer.BACK_TOP)) {
            tile.render(graphics, Layer.BACK_TOP);
        }
        for (Tile tile : getTiles(Layer.FRONT)) {
            tile.render(graphics, Layer.FRONT);
        }
        for (WorldObject object : objects) {
            if (object.getLocation().distanceSquared(camera.getLocation()) <= 640000) {
                object.render(graphics);
            }
        }
        graphics2D.translate(camera.getLocation().getX(), camera.getLocation().getY());
    }

    public Set<Tile> getTiles(Layer layer) {
        if (!tiles.containsKey(layer)) tiles.put(layer, new HashSet<Tile>());
        return tiles.get(layer);
    }

}
