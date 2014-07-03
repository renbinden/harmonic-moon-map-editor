package io.github.lucariatias.harmonicmoon.mapeditor.metadata;

import io.github.lucariatias.harmonicmoon.mapeditor.world.Location;

public class DoorMetadata extends WorldObjectMetadata {

    private Location entryLocation;

    public DoorMetadata(DoorMetadata original) {
        entryLocation = new Location(original.entryLocation);
    }

    public DoorMetadata() {
        entryLocation = new Location();
    }

}
