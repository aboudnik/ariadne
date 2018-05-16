package org.boudnik.ariadne;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author Alexandre_Boudnik
 * @since 05/04/2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceTest {
    @Mock
    private Resource resource;
    @Mock
    private Resource prerequisite;

    @Before
    public void setUp() {
        assertNotNull(resource);
        when(resource.prerequisites()).thenReturn(new HashSet<>(Collections.singletonList(prerequisite)));
        when(resource.type()).thenReturn("Test");
        when(resource.isSatisfied()).thenCallRealMethod();
        doThrow(new RuntimeException()).when(resource).build(null);

        when(prerequisite.isReady()).thenReturn(true);
    }

    @Test
    public void type() {
        assertSame("Test", resource.type());
    }

    @Test(expected = RuntimeException.class)
    public void build() {
        resource.build(null);
    }

    @Test
    public void prerequisites() {
        Set<Resource> prerequisites = resource.prerequisites();
        assertFalse(prerequisites.isEmpty());
        assertTrue(prerequisites.contains(prerequisite));
    }

    @Test
    public void isReady() {
        assertTrue(prerequisite.isReady());
        assertFalse(resource.isReady());
    }

    @Test
    public void isSatisfied() {
        assertTrue(resource.isSatisfied());
    }

    @After
    public void tearDown() {
    }
}