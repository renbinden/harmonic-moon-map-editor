package io.github.lucariatias.hmmapedit;

import java.awt.*;

public enum ObjectType {

    NOTHING(0, 0, 0),
    BLOCK(0, 0, 1),
    LONYRE(0, 0, 2);

    private final int r;
    private final int g;
    private final int b;

    private ObjectType(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color toColour() {
        return new Color(r, g, b);
    }

}
