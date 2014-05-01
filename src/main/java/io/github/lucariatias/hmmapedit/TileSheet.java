package io.github.lucariatias.hmmapedit;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TileSheet {

    private Camera camera;

    private BufferedImage image;
    private int tileWidth;
    private int tileHeight;
    private Tile[][] tileCache;

    public TileSheet(Camera camera, BufferedImage image, int tileWidth, int tileHeight) {
        this.camera = camera;
        this.image = image;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileCache = new Tile[image.getWidth() / tileWidth][image.getHeight() / tileHeight];
    }

    public BufferedImage getImage(int tileX, int tileY) {
        return image.getSubimage(tileX * tileWidth, tileY * tileHeight, tileWidth, tileHeight);
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Tile getTile(int tileX, int tileY) {
        if (tileCache[tileX][tileY] == null) {
            tileCache[tileX][tileY] = new Tile(camera, getImage(tileX, tileY), tileX, tileY);
        }
        return tileCache[tileX][tileY];
    }

    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int y = 0; y < image.getHeight() / tileHeight; y++) {
            for (int x = 0; x < image.getWidth() / tileWidth; x++) {
                tiles.add(getTile(x, y));
            }
        }
        return tiles;
    }

}
