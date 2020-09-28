package com.example.runtime.model;

import java.util.Objects;

public class ModelWithID<T> {
    private T model;
    private String id;

    public ModelWithID(String id, T model) {
        this.id = id;
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelWithID<?> that = (ModelWithID<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
