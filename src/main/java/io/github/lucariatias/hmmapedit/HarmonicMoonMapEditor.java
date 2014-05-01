package io.github.lucariatias.hmmapedit;

import javax.swing.*;
import java.awt.*;

public class HarmonicMoonMapEditor {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MapFrame mapFrame = new MapFrame();
                mapFrame.setVisible(true);
                TileFrame tileFrame = new TileFrame(mapFrame);
                tileFrame.setLocation(mapFrame.getX() + mapFrame.getWidth(), mapFrame.getY());
                tileFrame.setVisible(true);
            }
        });

    }

}
