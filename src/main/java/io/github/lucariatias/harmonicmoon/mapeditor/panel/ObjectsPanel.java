package io.github.lucariatias.harmonicmoon.mapeditor.panel;

import io.github.lucariatias.harmonicmoon.mapeditor.MapEditorFrame;
import io.github.lucariatias.harmonicmoon.mapeditor.metadata.SettableField;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.world.WorldObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;

public class ObjectsPanel extends JPanel {

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

    private JComboBox<WorldObject.Type> comboBoxObjectType;
    private JTree metadataTree;
    private JScrollPane scrollPane;
    private MetadataModifyPanel activeFieldModifier;

    private WorldObject activeObject;
    private SettableField activeField;

    public ObjectsPanel(MapEditorFrame frame) {
        this.frame = frame;
        setLayout(null);
        comboBoxObjectType = new JComboBox<>(WorldObject.Type.values());
        comboBoxObjectType.setBounds(4, 4, 240, 24);
        comboBoxObjectType.addActionListener(event -> {
            activeObject = new WorldObject(getSelectedObjectType());
            update();
        });
        add(comboBoxObjectType);
        activeObject = new WorldObject(getSelectedObjectType());
        EventQueue.invokeLater(this::update);
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
    }

    public WorldObject.Type getSelectedObjectType() {
        return (WorldObject.Type) comboBoxObjectType.getSelectedItem();
    }

    public void update() {
        if (activeObject != null) {
            if (scrollPane != null) remove(scrollPane);
            DefaultMutableTreeNode metadata = new DefaultMutableTreeNode(getSelectedObjectType().toString().toLowerCase());
            addFields(metadata, activeObject.getMetadata());
            metadataTree = new JTree(metadata);
            scrollPane = new JScrollPane(metadataTree);
            scrollPane.setBounds(4, 32, getWidth() - 8, ((getHeight() - 8) / 2) - 4);
            add(scrollPane);
            metadataTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            metadataTree.addTreeSelectionListener(event -> {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) metadataTree.getLastSelectedPathComponent();
                if (node == null) return;
                if (node.isLeaf() && node.getUserObject() instanceof SettableField && SETTABLE_TYPES.contains(((SettableField) node.getUserObject()).getField().getType())) {
                    setActiveField((SettableField) node.getUserObject());
                }
            });
        }
        EventQueue.invokeLater(this::revalidate);
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

    private java.util.List<Field> getFields(Class<?> clazz) {
        java.util.List<Field> fields = new ArrayList<>();
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
        activeFieldModifier.setBounds(4, 40 + scrollPane.getHeight(), getWidth() - 8, getHeight() - (48 + scrollPane.getHeight()));
        add(activeFieldModifier);
        repaint();
    }

    public SettableField getActiveField() {
        return activeField;
    }

    public WorldObject getActiveObject() {
        return activeObject;
    }

}
