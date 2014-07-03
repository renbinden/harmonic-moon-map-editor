package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.SettableField;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class WorldMetadataEditorPanel extends JPanel {

    private static final Set<Class<?>> SETTABLE_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class,
            boolean.class,
            Character.class,
            char.class,
            Byte.class,
            byte.class,
            Short.class,
            short.class,
            Integer.class,
            int.class,
            Long.class,
            long.class,
            Float.class,
            float.class,
            Double.class,
            double.class,
            String.class,
            String[].class
    ));

    private MapEditorFrame frame;

    private JScrollPane scrollPane;
    private JTree metadataTree;
    private MetadataModifyPanel activeFieldModifier;

    private SettableField activeField;

    public WorldMetadataEditorPanel(MapEditorFrame frame) {
        setLayout(null);
        this.frame = frame;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                if (scrollPane != null) {
                    scrollPane.setBounds(4, 4, 120, ((getHeight() - 8) / 2) - 4);
                    if (activeFieldModifier != null) activeFieldModifier.setBounds(4, 12 + scrollPane.getHeight(), getWidth() - 8, getHeight() - (16 + scrollPane.getHeight()));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Theme theme = frame.getTheme();
        graphics.setColor(theme.getBackgroundColour());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(theme.getContainerBackgroundColour());
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
    }

    public void update() {
        if (frame.getMapPanel().getWorld() != null) {
            if (scrollPane != null) remove(scrollPane);
            DefaultMutableTreeNode metadata = new DefaultMutableTreeNode("world");
            addFields(metadata, frame.getMapPanel().getWorld().getMetadata());
            metadataTree = new JTree(metadata);
            scrollPane = new JScrollPane(metadataTree);
            scrollPane.setBounds(4, 4, 120, ((getHeight() - 8) / 2) - 4);
            add(scrollPane);
            metadataTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            metadataTree.addTreeSelectionListener(event -> {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) metadataTree.getLastSelectedPathComponent();
                if (node == null) return;
                if (node.isLeaf()) {
                    setActiveField((SettableField) node.getUserObject());
                }
            });

        }
        repaint();
    }

    private void addFields(DefaultMutableTreeNode node, Object object) {
        if (object != null && object.getClass() != null && !SETTABLE_TYPES.contains(object.getClass())) {
            for (Field field : getFields(object.getClass())) {
                try {
                    field.setAccessible(true);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new SettableField(object, field));
                    addFields(newNode, field.get(object));
                    node.add(newNode);
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public void setActiveField(SettableField activeField) {
        this.activeField = activeField;
        if (activeFieldModifier != null) remove(activeFieldModifier);
        activeFieldModifier = new MetadataModifyPanel(frame, activeField);
        activeFieldModifier.setBounds(4, 12 + scrollPane.getHeight(), getWidth() - 8, getHeight() - (16 + scrollPane.getHeight()));
        add(activeFieldModifier);
        repaint();
    }

    public SettableField getActiveField() {
        return activeField;
    }

}
