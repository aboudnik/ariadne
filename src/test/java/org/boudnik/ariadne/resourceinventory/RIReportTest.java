package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Loader;
import org.boudnik.ariadne.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
@RunWith(MockitoJUnitRunner.class)
public class RIReportTest {
    private RIResource riResource = new RIResource();
    private Collection<Device32ports> devices;

    @Before
    public void setUp() {

         devices = new ArrayList<Device32ports>(){{
            add(new Device32ports(new RILocation("region1", "city1", "building1", "rack1", "device1")));
            add(new Device32ports(new RILocation("region1", "city1", "building1", "rack1", "device2")));
            add(new Device32ports(new RILocation("region1", "city1", "building1", "rack2", "device1")));
            add(new Device32ports(new RILocation("region1", "city1", "building1", "rack2", "device2")));
            add(new Device32ports(new RILocation("region1", "city2", "building1", "rack1", "device1")));
            add(new Device32ports(new RILocation("region1", "city2", "building1", "rack1", "device2")));
            add(new Device32ports(new RILocation("region1", "city3", "building1", "rack2", "device2")));
        }};

        riResource.addLocationCondition(device32ports -> device32ports.getResourceInventoryLocation().getRegionId().equals("region1"));
        riResource.addLocationCondition(device32ports -> device32ports.getResourceInventoryLocation().getCityId().equals("city1"));
        riResource.addLocationCondition(device32ports -> device32ports.getResourceInventoryLocation().getBuildingId().equals("building1"));
    }

    @Test
    public void type() {
     //   assertSame("RIResource", riResource.type());
    }

    @Test
    public void build() {
        System.out.println("devices " + devices.size());
        riResource.build((Loader) () -> devices);

        System.out.println("start " + devices);
    }

    @Test
    public void prerequisites() {
       
      //  assertFalse(prerequisites.isEmpty());
       // assertTrue(prerequisites.contains(prerequisite));
    }

    @Test
    public void isReady() {
        //assertTrue(prerequisite.isReady());
        //assertFalse(riResource.isReady());
    }

    @Test
    public void isSatisfied() {
//        assertTrue(riResource.isSatisfied());
    }

    @After
    public void tearDown() {
    }
}
