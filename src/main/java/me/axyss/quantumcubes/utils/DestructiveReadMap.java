package me.axyss.quantumcubes.utils;

import java.util.HashMap;

public class DestructiveReadMap<K, V> {
    private final HashMap<K, V> map = new HashMap<>();

    public void insert(K key, V value) {
        map.put(key, value);
    }

    public V extract(K key) {
        V value = map.get(key);
        map.remove(key);
        return value;
    }
}