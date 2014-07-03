package io.github.lucariatias.harmonicmoon.mapeditor.theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private List<Theme> themes = new ArrayList<>();
    private Theme theme;

    public ThemeManager() {
        Theme dark = new DarkTheme();
        addTheme(dark);
        Theme light = new LightTheme();
        addTheme(light);
        setTheme(dark);
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void addTheme(Theme theme) {
        themes.add(theme);
    }

    public void removeTheme(Theme theme) {
        themes.remove(theme);
    }

    public Theme getTheme(String name) {
        for (Theme theme : themes) {
            if (theme.getName().equalsIgnoreCase(name)) return theme;
        }
        return null;
    }

}
