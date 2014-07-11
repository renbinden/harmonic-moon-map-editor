package io.github.lucariatias.harmonicmoon.mapeditor.tile;

import io.github.lucariatias.harmonicmoon.mapeditor.world.World;

import java.awt.image.BufferedImage;

public class Tile {

    private TileSheet tileSheet;
    private int column;
    private int row;
    private boolean selected;

    public Tile(TileSheet tileSheet, int column, int row) {
        this.tileSheet = tileSheet;
        this.column = column;
        this.row = row;
    }

    public TileSheet getTileSheet() {
        return tileSheet;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public PlacedTile place(World world, Layer layer, int x, int y) {
        PlacedTile tile;
        if (layer == Layer.BACK || column != 0 || row != 0) {
            tile = new PlacedTile(tileSheet, column, row, x, y);
        } else {
            tile = null;
        }
        world.getTiles(layer)[x][y] = tile;
        return tile;
    }

    public BufferedImage getImage() {
        return tileSheet.getTileImage(getColumn(), getRow());
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
