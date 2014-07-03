package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.Layer;

import javax.swing.*;
import java.awt.*;

public class LayerPanel extends JPanel {

    private MapEditorFrame frame;

    public LayerPanel(MapEditorFrame frame) {
        this.frame = frame;
        setLayout(null);
        int y = 4;
        ButtonGroup buttonGroup = new ButtonGroup();
        for (Layer layer : Layer.values()) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setBounds(36, y, 32, 16);
            checkBox.setSelected(true);
            checkBox.addActionListener(event -> {
                layer.setVisible(checkBox.isSelected());
                frame.getMapPanel().repaint();
            });
            add(checkBox);
            JRadioButton radioButton = new JRadioButton();
            radioButton.setBounds(4, y, 32, 16);
            radioButton.addActionListener(event -> {
                for (Layer layer1 : Layer.values()) {
                    layer1.setActive(false);
                }
                frame.getMapPanel().setObjectsActive(false);
                layer.setActive(radioButton.isSelected());
            });
            add(radioButton);
            buttonGroup.add(radioButton);
            y += 20;
        }
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBounds(36, y, 32, 16);
        checkBox.setSelected(true);
        checkBox.addActionListener(event -> {
            frame.getMapPanel().setObjectsVisible(checkBox.isSelected());
            frame.getMapPanel().repaint();
        });
        add(checkBox);
        JRadioButton radioButton = new JRadioButton();
        radioButton.setBounds(4, y, 32, 16);
        radioButton.addActionListener(event -> {
            for (Layer layer : Layer.values()) {
                layer.setActive(false);
            }
            frame.getMapPanel().setObjectsActive(radioButton.isSelected());
        });
        add(radioButton);
        buttonGroup.add(radioButton);
        radioButton.doClick();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        graphics.setColor(theme.getBoxUpTextColour());
        int y = 4;
        for (Layer layer : Layer.values()) {
            graphics.drawString(layer.toString().toLowerCase(), 68, y + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
            y += 20;
        }
        graphics.drawString("objects", 68, y + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
    }

}
