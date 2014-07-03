package io.github.lucariatias.harmonicmoon.mapeditor.metadata;

import java.lang.reflect.Field;

public class SettableField {

    private Object object;
    private Field field;

    public SettableField(Object object, Field field) {
        this.object = object;
        this.field = field;
        if (!field.isAccessible()) field.setAccessible(true);
    }

    public Object getObject() {
        return object;
    }

    public Field getField() {
        return field;
    }

    public Object get() {
        try {
            return getField().get(getObject());
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void set(Object value) {
        try {
            getField().set(getObject(), value);
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getField().getName();
    }
}
