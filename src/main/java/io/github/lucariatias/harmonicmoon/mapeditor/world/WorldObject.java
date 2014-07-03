package io.github.lucariatias.harmonicmoon.mapeditor.world;

import io.github.lucariatias.harmonicmoon.mapeditor.metadata.DoorMetadata;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.Metadata;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.NpcMetadata;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.WorldObjectMetadata;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class WorldObject {

    public enum Type {

        NOTHING(0, 0, 0),
        BLOCK(0, 0, 1),
        DOOR(0, 0, 2, DoorMetadata.class),

        LONYRE(0, 1, 0),
        TIVOR(0, 1, 1),
        KESOWA(0, 1, 2),
        NAMAPO(0, 1, 3),
        SYALAE(0, 1, 4),
        ANARIA(0, 1, 5),
        IDAIN(0, 1, 6),
        SEURI(0, 1, 7),

        NPC_GUARD(0, 1, 8, NpcMetadata.class);

        private Class<? extends Metadata> metadataClass;
        private Color colour;

        private Type(Color colour, Class<? extends Metadata> metadataClass) {
            this.metadataClass = metadataClass;
            this.colour = colour;
        }

        private Type(Color colour) {
            this(colour, null);
        }

        private Type(int r, int g, int b, Class<? extends Metadata> metadataClass) {
            this(new Color(r, g, b), metadataClass);
        }

        private Type(int r, int g, int b) {
            this(new Color(r, g, b));
        }

        public Color getColour() {
            return colour;
        }

        public int getRed() {
            return getColour().getRed();
        }

        public int getGreen() {
            return getColour().getGreen();
        }

        public int getBlue() {
            return getColour().getBlue();
        }

        public Class<? extends Metadata> getMetadataClass() {
            return metadataClass;
        }

        public static Type getType(int r, int g, int b) {
            for (Type type : Type.values()) {
                if (type.getRed() == r && type.getGreen() == g && type.getBlue() == b) return type;
            }
            return null;
        }

        public static Type getType(Color colour) {
            return getType(colour.getRed(), colour.getGreen(), colour.getBlue());
        }

    }

    private Type type;
    private WorldObjectMetadata metadata;

    public WorldObject(WorldObject original) {
        this.type = original.type;
        if (getType().getMetadataClass() != null) {
            try {
                this.metadata = (WorldObjectMetadata) getType().getMetadataClass().getConstructor(getType().getMetadataClass()).newInstance(original.metadata);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        }
    }

    public WorldObject(Type type) {
        this.type = type;
        if (getType().getMetadataClass() != null) {
            try {
                metadata = (WorldObjectMetadata) getType().getMetadataClass().getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        }
    }

    public boolean hasMetadata() {
        return getMetadata() != null && !getMetadata().isEmpty();
    }

    public WorldObjectMetadata getMetadata() {
        return metadata;
    }

    public Type getType() {
        return type;
    }

    public Color getColour() {
        return getType().getColour();
    }

}
