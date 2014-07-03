package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel {

    private MapEditorFrame frame;

    private String text;

    public InformationPanel(MapEditorFrame frame) {
        this.frame = frame;
        text = "";
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRoundRect(0, -8, getWidth() - 1, getHeight() - 1, 8, 8);
        graphics.setColor(theme.getBoxUpTextColour());
        graphics.drawString(getText(), 4, 4 + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

}
