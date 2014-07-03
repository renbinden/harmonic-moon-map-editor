package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class EditPanel extends JPanel {

    private MapEditorFrame frame;

    private TilesPanel tilesPanel;
    private ObjectsPanel objectsPanel;

    private int mouseX;
    private int mouseY;
    private boolean mouseInside = true;
    private boolean mousePressed;

    public EditPanel(MapEditorFrame frame) {
        this.frame = frame;
        setLayout(null);
        Rectangle bounds = new Rectangle(4, 24, 248, 249);
        tilesPanel = new TilesPanel(frame);
        tilesPanel.setBounds(bounds);
        add(tilesPanel);
        objectsPanel = new ObjectsPanel(frame);
        objectsPanel.setBounds(bounds);
        add(objectsPanel);
        objectsPanel.setVisible(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                Rectangle bounds = new Rectangle(4, 24, getWidth() - 8, getHeight() - 28);
                tilesPanel.setBounds(bounds);
                objectsPanel.setBounds(bounds);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent event) {
                updateMousePosition(event.getX(), event.getY());
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                updateMousePosition(event.getX(), event.getY());
            }

        });
        addMouseListener(new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                mousePressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                mousePressed = false;
                repaint();
                handleClick(event.getX(), event.getY());
            }

            @Override
            public void mouseExited(MouseEvent event) {
                mouseInside = false;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                mouseInside = true;
                repaint();
            }

        });
    }

    private void updateMousePosition(int x, int y) {
        mouseX = x;
        mouseY = y;
        repaint();
    }

    private void handleClick(int x, int y) {
        Rectangle tilesButtonBounds = new Rectangle(4, 4, (getWidth() / 2) - 8, 16);
        boolean tilesButtonContainsMouse = mouseInside && tilesButtonBounds.contains(x, y);
        if (tilesButtonContainsMouse) {
            objectsPanel.setVisible(false);
            tilesPanel.setVisible(true);
        }
        Rectangle objectsButtonBounds = new Rectangle((getWidth() / 2) + 4, 4, (getWidth() / 2) - 8, 16);
        boolean objectsButtonContainsMouse = mouseInside && objectsButtonBounds.contains(x, y);
        if (objectsButtonContainsMouse) {
            tilesPanel.setVisible(false);
            objectsPanel.setVisible(true);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        Rectangle tilesButtonBounds = new Rectangle(4, 4, (getWidth() / 2) - 8, 16);
        boolean tilesButtonContainsMouse = mouseInside && tilesButtonBounds.contains(mouseX, mouseY);
        graphics.setColor(getTilesPanel().isVisible() ? theme.getBoxDownBackgroundColour() : tilesButtonContainsMouse ? mousePressed ? theme.getBoxDownBackgroundColour() : theme.getBoxHoverBackgroundColour() : theme.getBoxUpBackgroundColour());
        graphics.fillRect((int) tilesButtonBounds.getX(), (int) tilesButtonBounds.getY(), (int) tilesButtonBounds.getWidth(), (int) tilesButtonBounds.getHeight());
        graphics.setColor(getTilesPanel().isVisible() ? theme.getBoxDownEdgeColour() : tilesButtonContainsMouse ? mousePressed ? theme.getBoxDownEdgeColour() : theme.getBoxHoverEdgeColour() : theme.getBoxUpEdgeColour());
        graphics.drawRect((int) tilesButtonBounds.getX(), (int) tilesButtonBounds.getY(), (int) tilesButtonBounds.getWidth(), (int) tilesButtonBounds.getHeight());
        graphics.setColor(getTilesPanel().isVisible() ? theme.getBoxDownTextColour() : tilesButtonContainsMouse ? mousePressed ? theme.getBoxDownTextColour() : theme.getBoxHoverTextColour() : theme.getBoxUpTextColour());
        graphics.drawString("Tiles", (int) tilesButtonBounds.getX() + 4, (int) tilesButtonBounds.getY() + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
        Rectangle objectsButtonBounds = new Rectangle((getWidth() / 2) + 4, 4, (getWidth() / 2) - 8, 16);
        boolean objectsButtonContainsMouse = mouseInside && objectsButtonBounds.contains(mouseX, mouseY);
        graphics.setColor(getObjectsPanel().isVisible() ? theme.getBoxDownBackgroundColour() : objectsButtonContainsMouse ? mousePressed ? theme.getBoxDownBackgroundColour() : theme.getBoxHoverBackgroundColour() : theme.getBoxUpBackgroundColour());
        graphics.fillRect((int) objectsButtonBounds.getX(), (int) objectsButtonBounds.getY(), (int) objectsButtonBounds.getWidth(), (int) objectsButtonBounds.getHeight());
        graphics.setColor(getObjectsPanel().isVisible() ? theme.getBoxDownEdgeColour() : objectsButtonContainsMouse ? mousePressed ? theme.getBoxDownEdgeColour() : theme.getBoxHoverEdgeColour() : theme.getBoxUpEdgeColour());
        graphics.drawRect((int) objectsButtonBounds.getX(), (int) objectsButtonBounds.getY(), (int) objectsButtonBounds.getWidth(), (int) objectsButtonBounds.getHeight());
        graphics.setColor(getObjectsPanel().isVisible() ? theme.getBoxDownEdgeColour() : objectsButtonContainsMouse ? mousePressed ? theme.getBoxDownTextColour() : theme.getBoxHoverTextColour() : theme.getBoxUpTextColour());
        graphics.drawString("Objects", (int) objectsButtonBounds.getX() + 4, (int) objectsButtonBounds.getY() + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
    }

    public TilesPanel getTilesPanel() {
        return tilesPanel;
    }

    public ObjectsPanel getObjectsPanel() {
        return objectsPanel;
    }

}
