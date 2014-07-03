package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.Tile;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.TileSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TilesPanel extends JPanel {

    private MapEditorFrame frame;

    private JSpinner spinnerTileSheet;
    private JScrollPane scrollPane;
    private TileSheetPanel tileSheetPanel;

    public TilesPanel(MapEditorFrame frame) {
        this.frame = frame;
        setLayout(null);
        spinnerTileSheet = new JSpinner();
        spinnerTileSheet.setBounds(4, 4, 240, 24);
        spinnerTileSheet.addChangeListener(event -> {
            if ((int) spinnerTileSheet.getValue() >= 0 && (int) spinnerTileSheet.getValue() < frame.getMapPanel().getWorld().getTileSheets().size()) {
                tileSheetPanel.setTileSheet(frame.getMapPanel().getWorld().getTileSheets().get((int) spinnerTileSheet.getValue()));
                repaint();
            }
        });
        add(spinnerTileSheet);
        spinnerTileSheet.setVisible(false);
        tileSheetPanel = new TileSheetPanel(frame, null);
        scrollPane = new JScrollPane(tileSheetPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(4, 32, 240, 209);
        add(scrollPane);
        scrollPane.setVisible(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                getScrollPane().setBounds(4, 32, getWidth() - 8, getHeight() - 40);
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getBoxUpBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        graphics.setColor(theme.getBoxUpEdgeColour());
        graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
    }

    public void addTileSheet(TileSheet tileSheet) {
        if (frame.getMapPanel().getWorld().getTileSheets().size() > 0) {
            spinnerTileSheet.setVisible(true);
            spinnerTileSheet.setValue(frame.getMapPanel().getWorld().getTileSheets().indexOf(tileSheet));
            getTileSheetPanel().setTileSheet(tileSheet);
            getScrollPane().setVisible(true);
            spinnerTileSheet.setModel(new SpinnerNumberModel(0, 0, frame.getMapPanel().getWorld().getTileSheets().size() - 1, 1));
        } else {
            spinnerTileSheet.setVisible(false);
            getScrollPane().setVisible(false);
        }
        repaint();
        frame.getMapPanel().repaint();
    }

    public void refreshTileSheets() {
        if (frame.getMapPanel().getWorld().getTileSheets().size() > 0) {
            spinnerTileSheet.setVisible(true);
            tileSheetPanel.setTileSheet(frame.getMapPanel().getWorld().getTileSheets().get((int) spinnerTileSheet.getValue()));
            getScrollPane().setVisible(true);
            spinnerTileSheet.setModel(new SpinnerNumberModel(0, 0, frame.getMapPanel().getWorld().getTileSheets().size() - 1, 1));
        } else {
            spinnerTileSheet.setVisible(false);
            getScrollPane().setVisible(false);
        }
        repaint();
        frame.getMapPanel().repaint();
    }

    public TileSheetPanel getTileSheetPanel() {
        return tileSheetPanel;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public Tile getSelectedTile() {
        for (Tile tile : getTileSheetPanel().getTileSheet().getTiles()) {
            if (tile.isSelected()) return tile;
        }
        return null;
    }

}
