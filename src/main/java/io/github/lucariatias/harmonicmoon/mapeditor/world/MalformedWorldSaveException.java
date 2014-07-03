package io.github.lucariatias.harmonicmoon.mapeditor.world;

public class MalformedWorldSaveException extends Exception {

    public MalformedWorldSaveException() {
        super();
    }

    public MalformedWorldSaveException(String message) {
        super(message);
    }

    public MalformedWorldSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedWorldSaveException(Throwable cause) {
        super(cause);
    }

    public MalformedWorldSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
