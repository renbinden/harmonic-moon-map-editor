package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.SettableField;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MetadataModifyPanel extends JPanel {

    private MapEditorFrame frame;
    private SettableField field;

    public MetadataModifyPanel(MapEditorFrame frame, SettableField field) {
        this.frame = frame;
        this.field = field;
        setLayout(null);
        if (field.getField().getType() == Boolean.class || field.getField().getType() == boolean.class) {
            JToggleButton toggleButton = new JToggleButton();
            toggleButton.setBounds(4, 32, 112, 24);
            toggleButton.setSelected((boolean) field.get());
            toggleButton.setText("" + toggleButton.isSelected());
            toggleButton.addActionListener(event -> {
                field.set(toggleButton.isSelected());
                toggleButton.setText("" + toggleButton.isSelected());
            });
            add(toggleButton);
        } else if (field.getField().getType() == Character.class || field.getField().getType() == char.class) {
            JTextField textField = new JTextField(1);
            textField.setBounds(4, 32, 112, 24);
            textField.setText("" + (char) field.get());
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent event) {
                    if (textField.getText().length() > 0) field.set(textField.getText().charAt(0));
                }

                @Override
                public void removeUpdate(DocumentEvent event) {
                    if (textField.getText().length() > 0) field.set(textField.getText().charAt(0));
                }

                @Override
                public void changedUpdate(DocumentEvent event) {

                }
            });
            add(textField);
        } else if (field.getField().getType() == Byte.class || field.getField().getType() == byte.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((byte) field.get(), Byte.MIN_VALUE, Byte.MAX_VALUE, 1));
            spinner.setBounds(4, 32, 112, 24);
            spinner.addChangeListener(event -> field.set(spinner.getValue()));
            add(spinner);
        } else if (field.getField().getType() == Short.class || field.getField().getType() == short.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((short) field.get(), Short.MIN_VALUE, Short.MAX_VALUE, 1));
            spinner.setBounds(4, 32, 112, 24);
            spinner.addChangeListener(event -> field.set(spinner.getValue()));
            add(spinner);
        } else if (field.getField().getType() == Integer.class || field.getField().getType() == int.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((int) field.get(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            spinner.setBounds(4, 32, 112, 24);
            spinner.addChangeListener(event -> field.set(spinner.getValue()));
            add(spinner);
        } else if (field.getField().getType() == Long.class || field.getField().getType() == long.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((long) field.get(), Long.MIN_VALUE, Long.MAX_VALUE, 1));
            spinner.setBounds(4, 32, 112, 24);
            spinner.addChangeListener(event -> field.set(spinner.getValue()));
            add(spinner);
        } else if (field.getField().getType() == Float.class || field.getField().getType() == float.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((float) field.get(), Float.MIN_VALUE, Float.MAX_VALUE, 0.1F));
            spinner.setBounds(4, 32, 112, 24);
            spinner.addChangeListener(event -> field.set(spinner.getValue()));
            add(spinner);
        } else if (field.getField().getType() == Double.class || field.getField().getType() == double.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((double) field.get(), Double.MIN_VALUE, Double.MAX_VALUE, 0.1D));
            spinner.setBounds(4, 32, 112, 24);
            spinner.addChangeListener(event -> field.set(spinner.getValue()));
            add(spinner);
        } else if (field.getField().getType() == String.class) {
            JTextArea textArea = new JTextArea(field.get() != null ? (String) field.get() : "");
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBounds(4, 32, 232, 128);
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent event) {
                    field.set(textArea.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent event) {
                    field.set(textArea.getText());
                }

                @Override
                public void changedUpdate(DocumentEvent event) {

                }
            });
            add(scrollPane);
        } else if (field.getField().getType() == String[].class) {
            StringBuilder builder = new StringBuilder();
            for (String string : field.get() != null ? (String[]) field.get() : new String[] {}) {
                builder.append(string).append("\n");
            }
            JTextArea textArea = new JTextArea(builder.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBounds(4, 32, 232, 128);
            textArea.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent event) {
                    field.set(textArea.getText().split("\n"));
                }

                @Override
                public void removeUpdate(DocumentEvent event) {
                    field.set(textArea.getText().split("\n"));
                }

                @Override
                public void changedUpdate(DocumentEvent event) {

                }
            });
            add(scrollPane);
        }
        EventQueue.invokeLater(this::revalidate);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getBoxUpBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        graphics.setColor(theme.getBoxUpEdgeColour());
        graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        graphics.setColor(theme.getBoxUpTextColour());
        graphics.drawString(field.getField().getName() + ":", 4, 4 + graphics.getFontMetrics().getLeading() + graphics.getFontMetrics().getMaxAscent());
    }
}
