package org.apache.ariadne.cache.impl;

import org.apache.ariadne.cache.TieredCache;

import java.io.*;

/**
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
public class FileCache<K, V> extends AbstractTieredCache<K, V> {
    private final File dir;

    public FileCache(String name, TieredCache<K, V> base) {
        super(base);
        (dir = new File(name)).mkdirs();
    }

    @Override
    public V doGet(K key) {
        File file = new File(dir, key.toString());
        if (file.exists())
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                //noinspection unchecked
                return (V) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        return null;
    }

    @Override
    public void doPut(K key, V value) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, key.toString())))) {
            oos.writeObject(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean doRemove(K key) {
        return new File(dir, key.toString()).delete();
    }
}
