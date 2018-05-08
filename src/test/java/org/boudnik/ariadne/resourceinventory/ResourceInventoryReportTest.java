package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceInventoryReportTest {
    @Mock
    private Resource resource;

    private Resource prerequisite;
    @Mock
    private Collection<Device32ports> deviceCollection;

    @Before
    public void setUp() {
        assertNotNull(resource);
        LocationCondition locationCondition = new LocationCondition().setRegionId("region1").setCityId("cityId");
        Set<Resource> prerequisites = locationCondition.prerequisites();
        prerequisite = prerequisites.iterator().next();
        when(resource.prerequisites()).thenReturn(prerequisites);
        when(resource.type()).thenReturn(locationCondition.type());
        when(resource.isSatisfied()).thenCallRealMethod();
        doThrow(new RuntimeException()).when(resource).build();

        assertTrue(prerequisites.iterator().next().isReady());
    }

    @Test
    public void type() {
        assertSame("LocationPrerequisite", resource.type());
    }

    @Test(expected = RuntimeException.class)
    public void build() {
        resource.build();
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
