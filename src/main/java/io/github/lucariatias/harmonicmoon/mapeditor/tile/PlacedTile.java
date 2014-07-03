package io.github.lucariatias.harmonicmoon.mapeditor.tile;

public class PlacedTile extends Tile {

    private int x;
    private int y;

    public PlacedTile(TileSheet tileSheet, int column, int row, int x, int y) {
        super(tileSheet, column, row);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
