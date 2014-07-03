package io.github.lucariatias.harmonicmoon.mapeditor.tile;

public enum Layer {

    BACK, BACK_TOP, FRONT, FRONT_TOP;

    private boolean visible = true;
    private boolean active = false;

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

}
