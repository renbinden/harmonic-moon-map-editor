package io.github.lucariatias.harmonicmoon.mapeditor;

import io.github.lucariatias.harmonicmoon.mapeditor.panel.*;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.Theme;
import io.github.lucariatias.harmonicmoon.mapeditor.theme.ThemeManager;
import io.github.lucariatias.harmonicmoon.mapeditor.tile.TileSheet;
import io.github.lucariatias.harmonicmoon.mapeditor.world.MalformedWorldSaveException;
import io.github.lucariatias.harmonicmoon.mapeditor.world.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class MapEditorFrame extends JFrame {

    private ThemeManager themeManager;

    private File propertiesFile;
    private Properties properties;

    private File mapDirectory;

    private MapListPanel mapListPanel;
    private LayerPanel layerPanel;
    private EditPanel editPanel;
    private InformationPanel informationPanel;
    private WorldMetadataEditorPanel worldMetadataEditorPanel;
    private MapPanel mapPanel;

	/**
	 * Create the frame.
	 */
	public MapEditorFrame() {
        properties = new Properties();
        propertiesFile = new File("./hmme.properties");
        if (propertiesFile.exists()) {
            try {
                properties.load(new FileInputStream(propertiesFile));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            properties.setProperty("theme", "Dark");
            properties.setProperty("map-directory", System.getProperty("user.home"));
            try {
                properties.store(new FileOutputStream(propertiesFile), "");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        setTitle("Harmonic Moon Map Editor");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(548, 578));
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

        themeManager = new ThemeManager();
        themeManager.setTheme(themeManager.getTheme((String) properties.get("theme")));
        getContentPane().setBackground(getTheme().getBackgroundColour());

        setupMenuBar();

        mapDirectory = new File(properties.getProperty("map-directory"));

        worldMetadataEditorPanel = new WorldMetadataEditorPanel(this);
        worldMetadataEditorPanel.setBounds(6, 316, 128, 256);
        getContentPane().add(worldMetadataEditorPanel);

		mapListPanel = new MapListPanel(this);
		mapListPanel.setBounds(6, 6, 128, (getMapDirectory().listFiles(File::isDirectory).length * 20));
		getContentPane().add(mapListPanel);

        editPanel = new EditPanel(this);
        editPanel.setBounds(535, 295, 256, 277);
        getContentPane().add(editPanel);

        mapPanel = new MapPanel(this);
        mapPanel.setBounds(146, 38, 377, 534);
        getContentPane().add(mapPanel);
        worldMetadataEditorPanel.update();

        layerPanel = new LayerPanel(this);
        layerPanel.setBounds(535, 6, 256, 108);
		getContentPane().add(layerPanel);

        informationPanel = new InformationPanel(this);
        informationPanel.setBounds(146, 0, 377, 32);
        getContentPane().add(informationPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                getMapListPanel().setBounds(6, 6, 128, 8 + (getMapDirectory().listFiles(File::isDirectory).length * 20));
                getWorldMetadataEditorPanel().setBounds(6, 18 + getMapListPanel().getHeight(), 128, getContentPane().getHeight() - (getMapListPanel().getHeight() + 24));
                getInformationPanel().setBounds(18 + getMapListPanel().getWidth(), 0, getContentPane().getWidth() - (getMapListPanel().getWidth() + Math.max(getEditPanel().getWidth(), getLayerPanel().getWidth()) + 36), 32);
                getMapPanel().setBounds(18 + getMapListPanel().getWidth(), 6 + getInformationPanel().getHeight(), getContentPane().getWidth() - (getMapListPanel().getWidth() + Math.max(getEditPanel().getWidth(), getLayerPanel().getWidth()) + 36), getContentPane().getHeight() - (12 + getInformationPanel().getHeight()));
                getLayerPanel().setBounds(30 + getMapListPanel().getWidth() + getMapPanel().getWidth(), 6, 256, 108);
                getEditPanel().setBounds(30 + getMapListPanel().getWidth() + getMapPanel().getWidth(), 18 + getLayerPanel().getHeight(), 256, getContentPane().getHeight() - (24 + getLayerPanel().getHeight()));
            }
        });
        enableOSXFullscreen(this);
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveSettings));
	}

    private void setupMenuBar() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser(getMapDirectory());
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                setMapDirectory(fileChooser.getSelectedFile().getParentFile());
                try {
                    getMapPanel().setWorld(World.load(this, fileChooser.getSelectedFile()));
                    EventQueue.invokeLater(mapListPanel::repaint);
                    getEditPanel().getTilesPanel().refreshTileSheets();
                    getInformationPanel().setText("Loaded map '" + fileChooser.getSelectedFile().getName() + "'");
                } catch (MalformedWorldSaveException exception) {
                    getInformationPanel().setText("Failed to load map: " + exception.getMessage());
                }
            }
        });
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(openMenuItem);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser(getMapDirectory());
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                getMapPanel().getWorld().save(fileChooser.getSelectedFile());
            }
        });
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        JMenu themeMenu = new JMenu("Theme");
        for (Theme theme : themeManager.getThemes()) {
            JMenuItem themeMenuItem = new JMenuItem(theme.getName());
            themeMenuItem.addActionListener(event -> setTheme(theme));
            themeMenu.add(themeMenuItem);
        }
        menuBar.add(themeMenu);
        JMenu tileSheetsMenu = new JMenu("Tilesheets");
        JMenuItem addTileSheetMenuItem = new JMenuItem("Add");
        addTileSheetMenuItem.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".png");
                }

                @Override
                public String getDescription() {
                    return "Portable Network Graphics (*.png)";
                }
            });
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    TileSheet tileSheet = new TileSheet(ImageIO.read(fileChooser.getSelectedFile()), 16, 16); //TODO tile dimensions dialog
                    getMapPanel().getWorld().getTileSheets().add(tileSheet);
                    getEditPanel().getTilesPanel().addTileSheet(tileSheet);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this, "Failed to load tile sheet:\n" + exception.getMessage(), "Error loading tile sheet", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        tileSheetsMenu.add(addTileSheetMenuItem);
        menuBar.add(tileSheetsMenu);
        setJMenuBar(menuBar);
    }

    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ignore) {
        }
    }

    public MapListPanel getMapListPanel() {
        return mapListPanel;
    }

    public LayerPanel getLayerPanel() {
        return layerPanel;
    }

    public EditPanel getEditPanel() {
        return editPanel;
    }

    public InformationPanel getInformationPanel() {
        return informationPanel;
    }

    public WorldMetadataEditorPanel getWorldMetadataEditorPanel() {
        return worldMetadataEditorPanel;
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public Theme getTheme() {
        return themeManager.getTheme();
    }

    public void setTheme(Theme theme) {
        themeManager.setTheme(theme);
        getContentPane().setBackground(theme.getBackgroundColour());
        repaint();
    }

    public File getMapDirectory() {
        return mapDirectory;
    }

    public void setMapDirectory(File mapDirectory) {
        this.mapDirectory = mapDirectory;
        getMapListPanel().setBounds(6, 6, 128, 8 + (getMapDirectory().listFiles(File::isDirectory).length * 20));
        getWorldMetadataEditorPanel().setBounds(6, 18 + getMapListPanel().getHeight(), 128, getContentPane().getHeight() - (getMapListPanel().getHeight() + 24));
        getMapListPanel().repaint();
        getWorldMetadataEditorPanel().repaint();
    }

    public void saveSettings() {
        properties.setProperty("theme", getTheme().getName());
        properties.setProperty("map-directory", getMapDirectory().getPath());
        try {
            properties.store(new FileOutputStream(propertiesFile), "");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
