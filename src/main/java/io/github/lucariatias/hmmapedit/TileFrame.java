package io.github.lucariatias.hmmapedit;

import javax.imageio.ImageIO;
import javax.swing.*;
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
        comboBoxObject.setBounds(16, 16, 128, 32);
        add(comboBoxObject);
        comboBoxLayer = new JComboBox<>(Layer.values());
        comboBoxLayer.setBounds(144, 16, 128, 32);
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
        JMenuItem openMap = new JMenuItem("Open");
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
                        TileFrame.this.mapFrame.getMapPanel().setFrontTopTileMap(ImageIO.read(new File(fileChooser.getSelectedFile(), "tiles-front-top.png")));
                        TileFrame.this.mapFrame.repaint();
                        BufferedImage image = ImageIO.read(new File(fileChooser.getSelectedFile(), "tilesheet.png"));
                        TileSheet tileSheet = new TileSheet(TileFrame.this.mapFrame.getCamera(), image, 16, 16);
                        TileFrame.this.mapFrame.getMapPanel().setTileSheet(tileSheet);
                        TileFrame.this.mapFrame.repaint();
                        setSize(Math.max(image.getWidth(), 480), Math.max(image.getHeight() + 64, 480));
                        tilePanel.setSize(image.getWidth(), image.getHeight());
                        tilePanel.setTileSheet(tileSheet);
                        repaint();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(openMap);
        JMenuItem saveMap = new JMenuItem("Save");
        saveMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("os.name").toLowerCase().contains("mac") ? "/Volumes/Macintosh HD 2/harmonic-moon/harmonic-moon/src/main/resources/maps/" : "/home/ross/Documents/harmonic-moon/harmonic-moon/src/main/resources/maps/");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showSaveDialog(TileFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        if (!fileChooser.getSelectedFile().exists()) fileChooser.getSelectedFile().mkdir();
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getBackTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-back.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getBackTopTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-back-top.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getObjectMap(), "png", new File(fileChooser.getSelectedFile(), "objects.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getFrontTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-front.png"));
                        ImageIO.write(TileFrame.this.mapFrame.getMapPanel().getFrontTopTileMap(), "png", new File(fileChooser.getSelectedFile(), "tiles-front-top.png"));
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
        JMenu jumpMenu = new JMenu("Jump");
        JMenuItem coordJump = new JMenuItem("Co-ords");
        coordJump.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                TileFrame.this.mapFrame.getCamera().setLocation(new Location(Integer.parseInt(JOptionPane.showInputDialog("X:")), Integer.parseInt(JOptionPane.showInputDialog("Y:"))));
                TileFrame.this.mapFrame.repaint();
            }
        });
        jumpMenu.add(coordJump);
        menuBar.add(jumpMenu);
        setJMenuBar(menuBar);
    }

}
