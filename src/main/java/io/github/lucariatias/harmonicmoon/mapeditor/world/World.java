package io.github.lucariatias.harmonicmoon.mapeditor.world;

import com.google.gson.Gson;
import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.WorldMetadata;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.Layer;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.PlacedTile;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.TileSheet;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.TileSheetMetadata;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Scanner;

public class World {

    private MapEditorFrame frame;

    private WorldMetadata worldMetadata;
    private EnumMap<Layer, PlacedTile[][]> tiles;
    private WorldObject[][] objects;
    private List<TileSheet> tileSheets;

    private int cameraX;
    private int cameraY;

    public World(MapEditorFrame frame, int width, int height) {
        this.frame = frame;
        worldMetadata = new WorldMetadata();
        worldMetadata.setWidth(width);
        worldMetadata.setHeight(height);
        tiles = new EnumMap<>(Layer.class);
        objects = new WorldObject[width][height];
        tileSheets = new ArrayList<>();
    }

    private World(MapEditorFrame frame) {
        this.frame = frame;
    }

    public WorldMetadata getMetadata() {
        return worldMetadata;
    }

    public int getWidth() {
        return getMetadata().getWidth();
    }

    public int getHeight() {
        return getMetadata().getHeight();
    }

    public PlacedTile[][] getTiles(Layer layer) {
        if (!tiles.containsKey(layer)) tiles.put(layer, new PlacedTile[getMetadata().getWidth()][getMetadata().getHeight()]);
        return tiles.get(layer);
    }

    public PlacedTile getTileAt(Layer layer, int x, int y) {
        return getTiles(layer)[x][y];
    }

    public WorldObject[][] getObjects() {
        return objects;
    }

    public WorldObject getObjectAt(int x, int y) {
        return getObjects()[x][y];
    }

    public List<TileSheet> getTileSheets() {
        return tileSheets;
    }

    private int getTileSheetId(TileSheet tileSheet) {
        if (!tileSheets.contains(tileSheet)) tileSheets.add(tileSheet);
        return tileSheets.indexOf(tileSheet);
    }

    public void save(File file) {
        if (!file.isDirectory()) file.delete();
        if (!file.exists()) file.mkdirs();
        Gson gson = new Gson();
        File worldMetadataFile = new File(file, "metadata.json");
        if (worldMetadataFile.exists()) worldMetadataFile.delete();
        try {
            worldMetadataFile.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        String worldMetadataJson = gson.toJson(getMetadata());
        try {
            FileWriter worldMetadataWriter = new FileWriter(worldMetadataFile);
            worldMetadataWriter.write(worldMetadataJson);
            worldMetadataWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        File tilesDirectory = new File(file, "tiles");
        if (!tilesDirectory.isDirectory()) tilesDirectory.delete();
        if (!tilesDirectory.exists()) tilesDirectory.mkdirs();
        for (Layer layer : Layer.values()) {
            BufferedImage tiles = new BufferedImage(getMetadata().getWidth(), getMetadata().getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < getMetadata().getWidth(); x++) {
                for (int y = 0; y < getMetadata().getHeight(); y++) {
                    PlacedTile tile = getTiles(layer)[x][y];
                    if (tile != null) {
                        int r = tile.getColumn();
                        int g = tile.getRow();
                        int b = getTileSheetId(tile.getTileSheet());
                        tiles.setRGB(x, y, (r << 16) | (g << 8) | b);
                    }
                }
            }
            File tilesFile = new File(tilesDirectory, layer.toString().toLowerCase() + ".png");
            try {
                ImageIO.write(tiles, "png", tilesFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        File objectsDirectory = new File(file, "objects");
        if (!objectsDirectory.isDirectory()) objectsDirectory.delete();
        if (!objectsDirectory.exists()) objectsDirectory.mkdirs();
        File objectMetadataDirectory = new File(objectsDirectory, "metadata");
        if (!objectMetadataDirectory.isDirectory()) objectMetadataDirectory.delete();
        if (!objectMetadataDirectory.exists()) objectMetadataDirectory.mkdirs();
        BufferedImage objects = new BufferedImage(getMetadata().getWidth(), getMetadata().getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < getMetadata().getWidth(); x++) {
            for (int y = 0; y < getMetadata().getHeight(); y++) {
                WorldObject object = getObjects()[x][y];
                if (object != null) {
                    int r = object.getColour().getRed();
                    int g = object.getColour().getGreen();
                    int b = object.getColour().getBlue();
                    objects.setRGB(x, y, (r << 16) | (g << 8) | b);
                    File objectMetadataXDirectory = new File(objectMetadataDirectory, "" + x);
                    if (!objectMetadataXDirectory.isDirectory()) objectMetadataXDirectory.delete();
                    File objectMetadataYDirectory = new File(objectMetadataXDirectory, "" + y);
                    if (!objectMetadataYDirectory.isDirectory()) objectMetadataYDirectory.delete();
                    if (!objectMetadataYDirectory.exists()) objectMetadataYDirectory.mkdir();
                    File objectMetadataFile = new File(objectMetadataYDirectory, "metadata.json");
                    if (objectMetadataFile.exists()) objectMetadataFile.delete();
                    if (object.hasMetadata()) {
                        objectMetadataYDirectory.mkdirs();
                        try {
                            objectMetadataFile.createNewFile();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        String json = gson.toJson(object.getMetadata());
                        try {
                            FileWriter objectMetadataWriter = new FileWriter(objectMetadataFile);
                            objectMetadataWriter.write(json);
                            objectMetadataWriter.close();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        objectMetadataYDirectory.delete();
                    }
                    if (objectMetadataXDirectory.exists() && objectMetadataXDirectory.listFiles().length == 0)
                        objectMetadataXDirectory.delete();
                }
            }
        }
        File objectsFile = new File(objectsDirectory, "objects.png");
        try {
            ImageIO.write(objects, "png", objectsFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        File tileSheetsDirectory = new File(file, "tilesheets");
        if (!tileSheetsDirectory.isDirectory()) tileSheetsDirectory.delete();
        if (!tileSheetsDirectory.exists()) tileSheetsDirectory.mkdirs();
        for (int i = 0; i < getTileSheets().size(); i++) {
            File tileSheetDirectory = new File(tileSheetsDirectory, "" + i);
            if (!tileSheetDirectory.isDirectory()) tileSheetDirectory.delete();
            if (!tileSheetDirectory.exists()) tileSheetDirectory.mkdirs();
            File tileSheetImageFile = new File(tileSheetDirectory, "tilesheet.png");
            try {
                ImageIO.write(getTileSheets().get(i).getImage(), "png", tileSheetImageFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            File tileSheetMetadataFile = new File(tileSheetDirectory, "metadata.json");
            if (tileSheetMetadataFile.exists()) tileSheetMetadataFile.delete();
            try {
                tileSheetMetadataFile.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            String tileSheetMetadataJson = gson.toJson(getTileSheets().get(i).getMetadata());
            try {
                FileWriter tileSheetMetadataWriter = new FileWriter(tileSheetMetadataFile);
                tileSheetMetadataWriter.write(tileSheetMetadataJson);
                tileSheetMetadataWriter.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static World load(MapEditorFrame frame, File file) throws MalformedWorldSaveException {
        if (!file.exists() || !file.isDirectory()) throw new MalformedWorldSaveException("Save file does not exist.");
        Gson gson = new Gson();
        World world = new World(frame);
        File worldMetadataFile = new File(file, "metadata.json");
        if (!worldMetadataFile.exists()) throw new MalformedWorldSaveException("World metadata file does not exist.");
        if (worldMetadataFile.isDirectory())
            throw new MalformedWorldSaveException("World metadata file is a directory.");
        try {
            Scanner scanner = new Scanner(new FileInputStream(worldMetadataFile));
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }
            String json = builder.toString();
            world.worldMetadata = gson.fromJson(json, WorldMetadata.class);
        } catch (FileNotFoundException exception) {
            throw new MalformedWorldSaveException("World metadata file does not exist.", exception);
        }
        world.tileSheets = new ArrayList<>();
        File tileSheetsDirectory = new File(file, "tilesheets");
        if (!tileSheetsDirectory.exists())
            throw new MalformedWorldSaveException("Tile sheets directory does not exist.");
        if (!tileSheetsDirectory.isDirectory())
            throw new MalformedWorldSaveException("Tile sheets directory is not a directory.");
        for (File tileSheetDirectory : tileSheetsDirectory.listFiles(File::isDirectory)) {
            File tileSheetImageFile = new File(tileSheetDirectory, "tilesheet.png");
            File tileSheetMetadataFile = new File(tileSheetDirectory, "metadata.json");
            TileSheetMetadata metadata;
            try {
                Scanner scanner = new Scanner(new FileInputStream(tileSheetMetadataFile));
                StringBuilder builder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine()).append("\n");
                }
                String json = builder.toString();
                metadata = gson.fromJson(json, TileSheetMetadata.class);
            } catch (FileNotFoundException exception) {
                throw new MalformedWorldSaveException("Tile sheet metadata does not exist.", exception);
            }
            try {
                BufferedImage tileSheetImage = ImageIO.read(tileSheetImageFile);
                TileSheet tileSheet = new TileSheet(tileSheetImage, metadata.getTileWidth(), metadata.getTileHeight());
                world.getTileSheets().add(tileSheet);
                frame.getEditPanel().getTilesPanel().addTileSheet(tileSheet);
            } catch (IOException exception) {
                throw new MalformedWorldSaveException("Failed to load tile sheet image.", exception);
            }
        }
        world.tiles = new EnumMap<>(Layer.class);
        File tilesDirectory = new File(file, "tiles");
        if (!tilesDirectory.exists()) throw new MalformedWorldSaveException("Tiles directory does not exist.");
        if (!tilesDirectory.isDirectory()) throw new MalformedWorldSaveException("Tiles directory is not a directory.");
        for (Layer layer : Layer.values()) {
            File tilesFile = new File(tilesDirectory, layer.toString().toLowerCase() + ".png");
            if (!tilesFile.exists())
                throw new MalformedWorldSaveException("Tile map does not exist for layer " + layer.toString().toLowerCase() + ".");
            try {
                BufferedImage tiles = ImageIO.read(tilesFile);
                for (int x = 0; x < world.getWidth(); x++) {
                    for (int y = 0; y < world.getHeight(); y++) {
                        int colour = tiles.getRGB(x, y);
                        int r = (colour >> 16) & 0xff;
                        int g = (colour >> 8) & 0xff;
                        int b = colour & 0xff;
                        if (layer == Layer.BACK || r != 0 || g != 0 || b != 0) world.getTileSheets().get(b).getTile(r, g).place(world, layer, x, y);
                    }
                }
            } catch (IOException exception) {
                throw new MalformedWorldSaveException("Failed to load tile map for layer " + layer.toString().toLowerCase() + ".", exception);
            }
        }
        world.objects = new WorldObject[world.getWidth()][world.getHeight()];
        File objectsDirectory = new File(file, "objects");
        if (!objectsDirectory.exists()) throw new MalformedWorldSaveException("Objects directory does not exist.");
        if (!objectsDirectory.isDirectory())
            throw new MalformedWorldSaveException("Objects directory is not a directory.");
        File objectsFile = new File(objectsDirectory, "objects.png");
        if (!objectsFile.exists()) throw new MalformedWorldSaveException("Objects file does not exist.");
        if (objectsFile.isDirectory()) throw new MalformedWorldSaveException("Object file is a directory.");
        try {
            BufferedImage objects = ImageIO.read(objectsFile);
            for (int x = 0; x < world.getWidth(); x++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    int colour = objects.getRGB(x, y);
                    int r = (colour >> 16) & 0xff;
                    int g = (colour >> 8) & 0xff;
                    int b = colour & 0xff;
                    if (r != 0 || g != 0 || b != 0) {
                        WorldObject.Type objectType = WorldObject.Type.getType(r, g, b);
                        world.getObjects()[x][y] = new WorldObject(objectType);
                        File objectMetadataFile = new File(objectsDirectory.getPath() + File.separatorChar + "metadata" + File.separatorChar + x + File.separatorChar + y + File.separatorChar + "metadata.json");
                        if (objectMetadataFile.exists()) {
                            Scanner scanner = new Scanner(new FileInputStream(objectMetadataFile));
                            StringBuilder builder = new StringBuilder();
                            while (scanner.hasNextLine()) {
                                builder.append(scanner.nextLine()).append("\n");
                            }
                            String json = builder.toString();
                            gson.fromJson(json, objectType.getMetadataClass()).transfer(world.getObjects()[x][y].getMetadata());
                        }
                    }
                }
            }
        } catch (IOException exception) {
            throw new MalformedWorldSaveException("Failed to load objects.", exception);
        }
        return world;
    }

    public void render(Graphics graphics) {
        if (getTileSheets().size() > 0) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(-getCameraX(), -getCameraY());
            for (int x = Math.max((cameraX / getTileSheets().get(0).getTileWidth()) - 1, 0); x < Math.min(((cameraX + frame.getMapPanel().getWidth()) / getTileSheets().get(0).getTileWidth()) + 1, getWidth()); x++) {
                for (int y = Math.max((cameraY / getTileSheets().get(0).getTileHeight()) - 1, 0); y < Math.min(((cameraY + frame.getMapPanel().getHeight()) / getTileSheets().get(0).getTileHeight() + 1), getWidth()); y++) {
                    for (Layer layer : Layer.values()) {
                        if (layer.isVisible()) {
                            PlacedTile tile = getTileAt(layer, x, y);
                            if (tile != null) graphics.drawImage(tile.getImage(), x * getTileSheets().get(0).getTileWidth(), y * getTileSheets().get(0).getTileHeight(), null);
                        }
                    }
                    graphics.setColor(Color.RED);
                    if (frame.getMapPanel().isObjectsVisible()) {
                        WorldObject object = getObjectAt(x, y);
                        if (object != null) {
                            graphics.drawRect(x * getTileSheets().get(0).getTileWidth(), y * getTileSheets().get(0).getTileHeight(), getTileSheets().get(0).getTileWidth() - 1, getTileSheets().get(0).getTileHeight() - 1);
                        }
                    }
                }
            }
            graphics2D.translate(getCameraX(), getCameraY());
        }
    }

    public int getCameraX() {
        return cameraX;
    }

    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }

    public int getCameraY() {
        return cameraY;
    }

    public void setCameraY(int cameraY) {
        this.cameraY = cameraY;
    }

}
