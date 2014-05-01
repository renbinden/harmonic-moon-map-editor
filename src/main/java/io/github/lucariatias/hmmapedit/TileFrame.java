package io.github.lucariatias.hmmapedit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileFrame extends JFrame {

    private MapFrame mapFrame;
    private TilePanel tilePanel;

    JComboBox<ObjectType> comboBoxObject;
    JComboBox<Layer> comboBoxLayer;

	public TileFrame(MapFrame mapFrame) {
        this.mapFrame = mapFrame;
        mapFrame.setTileFrame(this);
		setLayout(null);
        setSize(320, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setupMenuBar();
        comboBoxObject = new JComboBox<>(ObjectType.values());
        comboBoxObject.setBounds(16, 16, 256, 32);
        add(comboBoxObject);
        comboBoxLayer = new JComboBox<>(Layer.values());
        comboBoxLayer.setBounds(272, 16, 256, 32);
        add(comboBoxLayer);
        tilePanel = new TilePanel(this, null);
        tilePanel.setBounds(0, 64, 256, 480);
        add(tilePanel);
	}

    public ObjectType getSelectedObject() {
        return (ObjectType) comboBoxObject.getSelectedItem();
    }

    public Layer getSelectedLayer() {
        return (Layer) comboBoxLayer.getSelectedItem();
    }

    private void setupMenuBar() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openTileSheet = new JMenuItem("Open tile sheet");
        openTileSheet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("os.name").toLowerCase().contains("mac") ? "/Volumes/Macintosh HD 2/harmonic-moon/harmonic-moon/src/main/resources/" : "/home/ross/Documents/harmonic-moon/harmonic-moon/src/main/resources/");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Portable Network Graphics", "png");
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(TileFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                        TileSheet tileSheet = new TileSheet(TileFrame.this.mapFrame.getCamera(), image, 16, 16);
                        TileFrame.this.mapFrame.getMapPanel().setTileSheet(tileSheet);
                        TileFrame.this.mapFrame.repaint();
                        setSize(image.getWidth(), image.getHeight() + 64);
                        tilePanel.setSize(image.getWidth(), image.getHeight());
                        tilePanel.setTileSheet(tileSheet);
                        repaint();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(openTileSheet);
        JMenuItem openMap = new JMenuItem("Open map");
        openMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("os.name").toLowerCase().contains("mac") ? "/Volumes/Macintosh HD 2/harmonic-moon/harmonic-moon/src/main/resources/maps/" : "/home/ross/Documents/harmonic-moon/harmonic-moon/src/main/resources/maps/");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(TileFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        TileFrame.this.mapFrame.getMapPanel().setBackTileMap(ImageIO.read(new File(fileChooser.getSelectedFile(), "tiles-back.png")));
                        TileFrame.this.mapFrame.getMapPanel().setBackTopTileMap(ImageIO.read(new File(fileChooser.getSelectedFile(), "tiles-back-top.png")));
                        TileFrame.this.mapFrame.getMapPanel().setObjectMap(ImageIO.read(new File(fileChooser.getSelectedFile(), "objects.png")));
                        TileFrame.this.mapFrame.getMapPanel().setFrontTileMap(ImageIO.read(new File(fileChooser.getSelectedFile(), "tiles-front.png")));
                        TileFrame.this.mapFrame.repaint();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(openMap);
        JMenuItem saveMap = new JMenuItem("Save map");
        saveMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("os.name").toLowerCase().contains("mac") ? "/Volumes/Macintosh HD 2/harmonic-moon/harmonic-moon/src/main/resources/maps/" : "/home/ross/Documents/harmonic-moon/harmonic-moon/src/main/resources/maps/");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showSaveDialog(TileFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getBackTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-back.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getBackTopTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-back-top.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getObjectMap(), "png", new File(fileChooser.getSelectedFile(), "objects.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getFrontTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-front.png"));
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(saveMap);
        menuBar.add(fileMenu);
        JMenu scrollingMenu = new JMenu("Scrolling");
        JMenuItem enableScrolling = new JMenuItem("Enable");
        enableScrolling.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                TileFrame.this.mapFrame.setScrollingEnabled(true);
            }
        });
        scrollingMenu.add(enableScrolling);
        JMenuItem disableScrolling = new JMenuItem("Disable");
        disableScrolling.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                TileFrame.this.mapFrame.setScrollingEnabled(false);
            }
        });
        scrollingMenu.add(disableScrolling);
        menuBar.add(scrollingMenu);
        setJMenuBar(menuBar);
    }

}
