package org.apache.ariadne.cache;

import org.apache.ariadne.cache.impl.FileCache;
import org.apache.ariadne.cache.impl.GridCache;
import org.apache.ariadne.cache.impl.JavaCache;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexandre_Boudnik
 * @since 09/07/2018
 */
@SuppressWarnings({"FieldCanBeLocal", "SimplifiableJUnitAssertion"})
public class TieredCacheTest {

    private TieredCache<Integer, Object> file;
    private TieredCache<Integer, Object> grid;
    private TieredCache<Integer, Object> java;
    private Ignite ignite;


    @Before
    public void setUp() {
        ignite = Ignition.start();
        file = new FileCache<>("file", null);
        grid = new GridCache<>("grid", file);
        java = new JavaCache<>(grid);
        file.put(1, "1");
        grid.put(2, "2");
        java.put(3, "3");
    }

    @Test
    public void get() {
        assertEquals("1", java.get(1));
        assertEquals("2", java.get(2));
        assertEquals("3", java.get(3));
        assertEquals(null, java.get(4));
        assertTrue(file.remove(1));
        assertEquals(null, java.get(1));
    }

    /**
     * Placeholder for write through.
     */
    @Ignore
    @Test
    public void put() {
        assertEquals("1", java.get(1));
        assertEquals("2", java.get(2));
        assertEquals("3", java.get(3));
        assertEquals(null, java.get(4));
        assertTrue(file.remove(1));
        assertEquals(null, java.get(1));
    }

    @After
    public void tearDown() {
        ignite.close();
    }

}