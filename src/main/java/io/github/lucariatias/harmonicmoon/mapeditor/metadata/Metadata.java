package io.github.lucariatias.harmonicmoon.mapeditor.metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Metadata {

    public void transfer(Metadata metadata) {
        if (getClass() == metadata.getClass()) {
            for (Field field : getFields()) {
                try {
                    field.setAccessible(true);
                    field.set(metadata, field.get(this));
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public boolean isEmpty() {
        try {
            for (Field field : getFields()) {
                field.setAccessible(true);
                if (field.get(this) != null) return false;
            }
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    private List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = getClass();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
