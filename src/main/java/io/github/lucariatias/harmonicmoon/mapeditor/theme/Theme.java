package io.github.lucariatias.harmonicmoon.mapeditor.theme;

import java.awt.*;

public abstract class Theme {

    private String name;

    private Color backgroundColour;
    private Color containerBackgroundColour;
    private Color boxUpEdgeColour;
    private Color boxHoverEdgeColour;
    private Color boxDownEdgeColour;
    private Color boxUpBackgroundColour;
    private Color boxHoverBackgroundColour;
    private Color boxDownBackgroundColour;
    private Color boxUpTextColour;
    private Color boxHoverTextColour;
    private Color boxDownTextColour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public Color getContainerBackgroundColour() {
        return containerBackgroundColour;
    }

    public void setContainerBackgroundColour(Color containerBackgroundColour) {
        this.containerBackgroundColour = containerBackgroundColour;
    }

    public Color getBoxUpEdgeColour() {
        return boxUpEdgeColour;
    }

    public void setBoxUpEdgeColour(Color boxUpEdgeColour) {
        this.boxUpEdgeColour = boxUpEdgeColour;
    }

    public Color getBoxHoverEdgeColour() {
        return boxHoverEdgeColour;
    }

    public void setBoxHoverEdgeColour(Color boxHoverEdgeColour) {
        this.boxHoverEdgeColour = boxHoverEdgeColour;
    }

    public Color getBoxDownEdgeColour() {
        return boxDownEdgeColour;
    }

    public void setBoxDownEdgeColour(Color boxDownEdgeColour) {
        this.boxDownEdgeColour = boxDownEdgeColour;
    }

    public Color getBoxUpBackgroundColour() {
        return boxUpBackgroundColour;
    }

    public void setBoxUpBackgroundColour(Color boxUpBackgroundColour) {
        this.boxUpBackgroundColour = boxUpBackgroundColour;
    }

    public Color getBoxHoverBackgroundColour() {
        return boxHoverBackgroundColour;
    }

    public void setBoxHoverBackgroundColour(Color boxHoverBackgroundColour) {
        this.boxHoverBackgroundColour = boxHoverBackgroundColour;
    }

    public Color getBoxDownBackgroundColour() {
        return boxDownBackgroundColour;
    }

    public void setBoxDownBackgroundColour(Color boxDownBackgroundColour) {
        this.boxDownBackgroundColour = boxDownBackgroundColour;
    }

    public Color getBoxUpTextColour() {
        return boxUpTextColour;
    }

    public void setBoxUpTextColour(Color boxUpTextColour) {
        this.boxUpTextColour = boxUpTextColour;
    }

    public Color getBoxHoverTextColour() {
        return boxHoverTextColour;
    }

    public void setBoxHoverTextColour(Color boxHoverTextColour) {
        this.boxHoverTextColour = boxHoverTextColour;
    }

    public Color getBoxDownTextColour() {
        return boxDownTextColour;
    }

    public void setBoxDownTextColour(Color boxDownTextColour) {
        this.boxDownTextColour = boxDownTextColour;
    }

}
