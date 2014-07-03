package io.github.lucariatias.harmonicmoon.mapeditor.metadata;

public class NpcMetadata extends WorldObjectMetadata {

    private String[] chatLines;

    public NpcMetadata(NpcMetadata original) {
        this.chatLines = original.chatLines;
    }

    public NpcMetadata() {}

}
