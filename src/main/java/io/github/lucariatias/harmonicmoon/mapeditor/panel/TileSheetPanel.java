package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.Tile;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.TileSheet;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TileSheetPanel extends JPanel {

    private MapEditorFrame frame;
    private TileSheet tileSheet;

    public TileSheetPanel(MapEditorFrame frame, TileSheet tileSheet) {
        this.frame = frame;
        this.tileSheet = tileSheet;
        if (tileSheet != null) setSize(tileSheet.getImage().getWidth(), tileSheet.getImage().getHeight());
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (getTileSheet() != null) {
                    for (Tile tile : getTileSheet().getTiles()) {
                        tile.setSelected(false);
                    }
                    getTileSheet().getTile(event.getX() / getTileSheet().getTileWidth(), event.getY() / getTileSheet().getTileHeight()).setSelected(true);
                    repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        if (getTileSheet() != null) getTileSheet().render(graphics, getTileSheet().getImage().getWidth());
    }

    public void setTileSheet(TileSheet tileSheet) {
        this.tileSheet = tileSheet;
        if (tileSheet != null) setPreferredSize(new Dimension(tileSheet.getImage().getWidth(), tileSheet.getImage().getHeight()));
        EventQueue.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    public TileSheet getTileSheet() {
        return tileSheet;
    }

}
