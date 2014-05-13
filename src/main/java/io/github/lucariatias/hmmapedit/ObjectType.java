package io.github.lucariatias.hmmapedit;

import java.awt.*;

public enum ObjectType {

    NOTHING(0, 0, 0),
    BLOCK(0, 0, 1),
    DOOR(0, 0, 2),
    LONYRE(0, 1, 0),
    TIVOR(0, 1, 1),
    KESOWA(0, 1, 2),
    NAMAPO(0, 1, 3),
    SYALAE(0, 1, 4),
    ANARIA(0, 1, 5),
    IDAIN(0, 1, 6),
    SEURI(0, 1, 7);


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
