package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.world.MalformedWorldSaveException;
import io.github.lucariatias.harmonicmoon.mapeditor.world.World;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.io.File;

public class MapListPanel extends JPanel {

    private MapEditorFrame frame;

    private int mouseX;
    private int mouseY;
    private boolean mouseInside = true;
    private boolean mousePressed;

    public MapListPanel(MapEditorFrame frame) {
        this.frame = frame;
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
        int optionY = 4;
        for (File file : frame.getMapDirectory().listFiles(File::isDirectory)) {
            boolean containsMouse = mouseInside && new Rectangle(4, optionY, getWidth() - 8, 16).contains(x, y);
            if (containsMouse) {
                try {
                    frame.getMapPanel().setWorld(World.load(frame, file));
                    frame.getEditPanel().getTilesPanel().refreshTileSheets();
                    frame.getInformationPanel().setText("Loaded map '" + file.getName() + "'");
                } catch (MalformedWorldSaveException exception) {
                    frame.getInformationPanel().setText("Failed to load map: " + exception.getMessage());
                }
            }
            optionY += 20;
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        int y = 4;
        for (File file : frame.getMapDirectory().listFiles(File::isDirectory)) {
            boolean containsMouse = mouseInside && new Rectangle(4, y, getWidth() - 8, 16).contains(mouseX, mouseY);
            graphics.setColor(containsMouse ? mousePressed ? theme.getBoxDownBackgroundColour() : theme.getBoxHoverBackgroundColour() : theme.getBoxUpBackgroundColour());
            graphics.fillRect(4, y, getWidth() - 8, 16);
            graphics.setColor(containsMouse ? mousePressed ? theme.getBoxDownEdgeColour() : theme.getBoxHoverEdgeColour() : theme.getBoxUpEdgeColour());
            graphics.drawRect(4, y, getWidth() - 8, 16);
            graphics.setColor(containsMouse ? mousePressed ? theme.getBoxDownTextColour() : theme.getBoxHoverTextColour() : theme.getBoxUpTextColour());
            GeneralPath triangle = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            triangle.moveTo(8, y + 2);
            triangle.lineTo(20, y + 8);
            triangle.lineTo(8, y + 14);
            triangle.closePath();
            ((Graphics2D) graphics).fill(triangle);
            graphics.drawString(file.getName(), 24, y + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
            y += 20;
        }
    }

}
