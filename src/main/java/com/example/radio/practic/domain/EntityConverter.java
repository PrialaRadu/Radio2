package com.example.radio.practic.domain;

public abstract class EntityConverter<T> {
    public abstract String toString(T shape);
    public abstract T fromString(String line);
}
