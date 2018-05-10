package org.boudnik.ariadne.abstractusecase;

import org.boudnik.ariadne.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTest {
    private ResourceImplementation resource;

    private Resource prerequisite;

    @Before
    public void setUp() {
        Prerequisite locationCondition = new Prerequisite().setParameter1("parameter1").setParameter2("parameter2");
        OutputParameters OutputParameters = new OutputParameters().setOutputParameters(new String[]{"output1", "output2"});
        resource = new ResourceImplementation().setLimitation(locationCondition).setOutputParameters(OutputParameters);

        Set<Resource> prerequisites = resource.prerequisites();
        prerequisite = prerequisites.iterator().next();

        assertTrue(prerequisites.iterator().next().isReady());
    }

    @Test
    public void type() {
        assertSame("ResourceImplementation", resource.type());
    }

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
        assertTrue(resource.isReady());
    }

    @Test
    public void isSatisfied() {
        assertTrue(resource.isSatisfied());
    }

    @After
    public void tearDown() {
    }
}

