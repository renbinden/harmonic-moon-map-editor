package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.Layer;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.Tile;
import io.github.lucariatias.harmonicmoon.mapeditor.world.World;
import io.github.lucariatias.harmonicmoon.mapeditor.world.WorldObject;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MapPanel extends JPanel {

    private MapEditorFrame frame;

    private World world;

    private boolean objectsVisible = true;
    private boolean objectsActive;

    private boolean mousePressed;
    private int initialMouseX;
    private int initialMouseY;
    private int finalMouseX;
    private int finalMouseY;

    public MapPanel(MapEditorFrame frame) {
        this.frame = frame;
        this.world = new World(frame, 64, 64);
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                handleMouseClick(event);
                if (SwingUtilities.isRightMouseButton(event)) mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) mousePressed = false;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                handleMouseClick(event);
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                initialMouseX = event.getX();
                initialMouseY = event.getY();
                if (frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet() != null) {
                    int worldX = (event.getX() + getWorld().getCameraX()) / frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet().getTileWidth();
                    int worldY = (event.getY() + getWorld().getCameraY()) / frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet().getTileHeight();
                    WorldObject object = getWorld().getObjectAt(worldX, worldY);
                    if (object != null) {
                        setToolTipText(object.getType().toString().toLowerCase().replace('_', ' '));
                    } else {
                        setToolTipText(null);
                    }
                }
            }
        });
        new Timer(100, event -> {
            if (mousePressed) {
                int dx = finalMouseX - initialMouseX;
                int dy = finalMouseY - initialMouseY;
                getWorld().setCameraX(getWorld().getCameraX() + (dx / 16));
                getWorld().setCameraY(getWorld().getCameraY() + (dy / 16));
                repaint();
            }
        }).start();
    }

    private void handleMouseClick(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            if (isObjectsActive()) {
                int worldX = (event.getX() + getWorld().getCameraX()) / frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet().getTileWidth();
                int worldY = (event.getY() + getWorld().getCameraY()) / frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet().getTileHeight();
                WorldObject object = frame.getEditPanel().getObjectsPanel().getActiveObject();
                if (frame.getEditPanel().getObjectsPanel().getSelectedObjectType() != WorldObject.Type.NOTHING) {
                    getWorld().getObjects()[worldX][worldY] = new WorldObject(object);
                } else {
                    getWorld().getObjects()[worldX][worldY] = null;
                }
                frame.getInformationPanel().setText("Placed " + object.getType().toString().toLowerCase().replace('_', ' ') + " at " + worldX + ", " + worldY);
            } else {
                Tile tile = frame.getEditPanel().getTilesPanel().getSelectedTile();
                if (tile != null) {
                    for (Layer layer : Layer.values()) {
                        if (layer.isActive()) {
                            int worldX = (event.getX() + getWorld().getCameraX()) / frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet().getTileWidth();
                            int worldY = (event.getY() + getWorld().getCameraY()) / frame.getEditPanel().getTilesPanel().getTileSheetPanel().getTileSheet().getTileHeight();
                            tile.place(getWorld(), layer, worldX, worldY);
                            frame.getInformationPanel().setText("Placed a tile at " + worldX + ", " + worldY + " on layer '" + layer.toString().toLowerCase() + "'");
                            break;
                        }
                    }
                }
            }
        } else if (SwingUtilities.isRightMouseButton(event)) {
            finalMouseX = event.getX();
            finalMouseY = event.getY();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        getWorld().render(graphics);
        if (mousePressed) {
            graphics.setColor(Color.YELLOW);
            graphics.drawOval(initialMouseX - 16, initialMouseY - 16, 32, 32);
            graphics.drawLine(initialMouseX, initialMouseY, finalMouseX, finalMouseY);
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        frame.getWorldMetadataEditorPanel().update();
    }

    public void setObjectsVisible(boolean objectsVisible) {
        this.objectsVisible = objectsVisible;
    }

    public boolean isObjectsVisible() {
        return objectsVisible;
    }

    public void setObjectsActive(boolean objectsActive) {
        this.objectsActive = objectsActive;
    }

    public boolean isObjectsActive() {
        return objectsActive;
    }
}
