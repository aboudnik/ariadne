package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
@RunWith(MockitoJUnitRunner.class)
public class RIReportTest {
    private RIResource riResource;

    private Resource prerequisite;

    @Before
    public void setUp() {
        LocationCondition locationCondition = new LocationCondition().setRegionId("region1").setCityId("cityId");
        RIReportOutputColumns riReportOutputColumns = new RIReportOutputColumns().setColumn_names(new String[]{"DeviceId", "PhysicalStatus", "SoftwareVersion", "LogicalStatus"});
        riResource = new RIResource().setLocation(locationCondition).setOutputColumns(riReportOutputColumns);

        Set<Resource> prerequisites = riResource.prerequisites();
        prerequisite = prerequisites.iterator().next();

        assertTrue(prerequisites.iterator().next().isReady());
    }

    @Test
    public void type() {
        assertSame("RIResource", riResource.type());
    }

    @Test(expected = RuntimeException.class)
    public void build() {
        riResource.build();
    }

    @Test
    public void prerequisites() {
        Set<Resource> prerequisites = riResource.prerequisites();
        assertFalse(prerequisites.isEmpty());
        assertTrue(prerequisites.contains(prerequisite));
    }

    @Test
    public void isReady() {
        assertTrue(prerequisite.isReady());
        //assertFalse(riResource.isReady());
    }

    @Test
    public void isSatisfied() {
        assertTrue(riResource.isSatisfied());
    }

    @After
    public void tearDown() {
    }
}
