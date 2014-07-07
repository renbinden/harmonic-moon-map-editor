package io.github.lucariatias.harmonicmoon.mapeditor.metadata;

public class NPCMetadata extends WorldObjectMetadata {

    private String[] chatLines;

    public NPCMetadata(NPCMetadata original) {
        this.chatLines = original.chatLines;
    }

    public NPCMetadata() {}

}
