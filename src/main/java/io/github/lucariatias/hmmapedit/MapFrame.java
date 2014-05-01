package io.github.lucariatias.hmmapedit;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MapFrame extends JFrame {

    private Camera camera;
    private MapPanel mapPanel;

    private TileFrame tileFrame;

    private boolean scrollingEnabled;

	public MapFrame() {
		getContentPane().setLayout(null);
		setSize(640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        camera = new Camera();
        camera.setLocation(new Location(0, 0));
        mapPanel = new MapPanel(this, camera, null, null, null, null, null);
        mapPanel.setBounds(0, 0, 640, 480);
        getContentPane().add(mapPanel);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!(MapFrame.this.isVisible() && MapFrame.this.isActive() && mapPanel.getBackTileMap() != null && mapPanel.getObjectMap() != null && mapPanel.getFrontTileMap() != null && mapPanel.getTileSheet() != null && scrollingEnabled)) return;
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                if (!MapFrame.this.getBounds().contains(mousePoint)) return;
                if (mousePoint.getX() - MapFrame.this.getLocationOnScreen().getX() > getWidth() - 64) {
                    camera.setLocation(camera.getLocation().getRelative(16, 0));
                }
                if (mousePoint.getX() - MapFrame.this.getLocationOnScreen().getX() < 64) {
                    camera.setLocation(camera.getLocation().getRelative(-16, 0));
                }
                if (mousePoint.getY() - MapFrame.this.getLocationOnScreen().getY() > getHeight() - 64) {
                    camera.setLocation(camera.getLocation().getRelative(0, 16));
                }
                if (mousePoint.getY() - MapFrame.this.getLocationOnScreen().getY() < 64) {
                    camera.setLocation(camera.getLocation().getRelative(0, -16));
                }
                MapFrame.this.repaint();
            }
        }, 0L, 250L);
	}

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public void setTileFrame(TileFrame tileFrame) {
        this.tileFrame = tileFrame;
    }

    public TileFrame getTileFrame() {
        return tileFrame;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isScrollingEnabled() {
        return scrollingEnabled;
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }

}
