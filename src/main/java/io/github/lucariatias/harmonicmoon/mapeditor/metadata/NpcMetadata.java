package io.github.lucariatias.harmonicmoon.mapeditor.metadata;

public class NPCMetadata extends WorldObjectMetadata {

    private String initialiseScript;
    private String interactScript;

    public NPCMetadata(NPCMetadata original) {
        this.initialiseScript = original.initialiseScript;
        this.interactScript = original.interactScript;
    }

    public NPCMetadata() {}

}
