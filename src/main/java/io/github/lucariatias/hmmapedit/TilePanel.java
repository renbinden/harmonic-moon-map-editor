package io.github.lucariatias.hmmapedit;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TilePanel extends JPanel {

    private TileFrame tileFrame;

    private TileSheet tileSheet;

    public TilePanel(TileFrame tileFrame, TileSheet tileSheet) {
        setLayout(null);
        this.tileFrame = tileFrame;
        this.tileSheet = tileSheet;
        tileFrame.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (TilePanel.this.tileSheet != null) {
                    int x = 0;
                    int y = 0;
                    for (Tile tile : TilePanel.this.tileSheet.getTiles()) {
                        if (event.getXOnScreen() >= x + TilePanel.this.getLocationOnScreen().getX()
                                && event.getXOnScreen() < x + TilePanel.this.getLocationOnScreen().getX() + TilePanel.this.tileSheet.getTileWidth()
                                && event.getYOnScreen() >= y + TilePanel.this.getLocationOnScreen().getY()
                                && event.getYOnScreen() < y + TilePanel.this.getLocationOnScreen().getY() + TilePanel.this.tileSheet.getTileHeight()) {
                            tile.setSelected(true);
                        } else {
                            tile.setSelected(false);
                        }
                        x += TilePanel.this.tileSheet.getTileWidth();
                        if (x >= getWidth()) {
                            x = 0;
                            y += 16;
                        }
                    }
                    TilePanel.this.repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (tileSheet != null) {
            int x = 0;
            int y = 0;
            for (Tile tile : tileSheet.getTiles()) {
                graphics.setColor(Color.WHITE);
                graphics.fillRect(x, y, 8, 8);
                graphics.fillRect(x + 8, y + 8, 8, 8);
                graphics.setColor(Color.GRAY);
                graphics.fillRect(x + 8, y, 8, 8);
                graphics.fillRect(x, y + 8, 8, 8);
                graphics.drawImage(tile.getImage(), x, y, null);
                x += tileSheet.getTileWidth();
                if (x >= getWidth()) {
                    x = 0;
                    y += 16;
                }
            }
            x = 0;
            y = 0;
            for (Tile tile : tileSheet.getTiles()) {
                if (tile.isSelected()) {
                    graphics.setColor(Color.RED);
                    graphics.drawRect(x, y, tileSheet.getTileWidth() - 1, tileSheet.getTileHeight() - 1);
                }
                x += tileSheet.getTileWidth();
                if (x >= getWidth()) {
                    x = 0;
                    y += 16;
                }
            }
        }
    }

    public void setTileSheet(TileSheet tileSheet) {
        this.tileSheet = tileSheet;
        repaint();
    }
}
