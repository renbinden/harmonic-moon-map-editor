package io.github.lucariatias.harmonicmoon.mapeditor.tile;

import io.github.lucariatias.harmonicmoon.mapeditor.metadata.TileSheetMetadata;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileSheet {

    private BufferedImage image;
    private TileSheetMetadata metadata;

    private Tile[][] tiles;
    private BufferedImage[][] tileImages;

    public TileSheet(BufferedImage image, int tileWidth, int tileHeight) {
        this.image = image;
        metadata = new TileSheetMetadata();
        metadata.setTileWidth(tileWidth);
        metadata.setTileHeight(tileHeight);
        tiles = new Tile[image.getWidth() / tileWidth][image.getHeight() / tileHeight];
        tileImages = new BufferedImage[image.getWidth() / tileWidth][image.getHeight() / tileHeight];
    }

    public BufferedImage getImage() {
        return image;
    }

    public TileSheetMetadata getMetadata() {
        return metadata;
    }

    public int getTileWidth() {
        return getMetadata().getTileWidth();
    }

    public int getTileHeight() {
        return getMetadata().getTileHeight();
    }

    public Tile getTile(int column, int row) {
        Tile tile;
        if (tiles[column][row] != null) {
            tile = tiles[column][row];
        } else {
            tile = new Tile(this, column, row);
            tiles[column][row] = tile;
        }
        return tile;
    }

    public BufferedImage getTileImage(int column, int row) {
        BufferedImage image;
        if (tileImages[column][row] != null) {
            image = tileImages[column][row];
        } else {
            image = getImage().getSubimage(column * getTileWidth(), row * getTileHeight(), getTileWidth(), getTileHeight());
            tileImages[column][row] = image;
        }
        return image;
    }

    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int row = 0; row < getImage().getHeight() / getTileHeight(); row++) {
            for (int col = 0; col < getImage().getWidth() / getTileWidth(); col++) {
                tiles.add(getTile(col, row));
            }
        }
        return tiles;
    }

    public void render(Graphics graphics, int maxWidth) {
        int x = 0;
        int y = 0;
        for (Tile tile : getTiles()) {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(x, y, 8, 8);
            graphics.fillRect(x + 8, y + 8, 8, 8);
            graphics.setColor(Color.GRAY);
            graphics.fillRect(x + 8, y, 8, 8);
            graphics.fillRect(x, y + 8, 8, 8);
            graphics.drawImage(tile.getImage(), x, y, null);
            if (tile.isSelected()) {
                graphics.setColor(Color.RED);
                graphics.drawRect(x, y, getTileWidth() - 1, getTileHeight() - 1);
            }
            x += getTileWidth();
            if (x >= maxWidth) {
                x = 0;
                y += getTileHeight();
            }
        }
    }

}
