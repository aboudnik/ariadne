package org.boudnik.ariadne.abstractusecase;

import org.boudnik.ariadne.Resource;

import java.util.Collections;
import java.util.Set;

public class ResourceImplementation implements Resource {

    private Prerequisite limitation;
    private OutputParameters outputParameters;

    public ResourceImplementation setOutputParameters(OutputParameters outputParameters) {
        this.outputParameters = outputParameters;
        return this;
    }

    public ResourceImplementation setLimitation(Prerequisite limitation) {
        this.limitation = limitation;
        return this;
    }

    @Override
    public String type() {
        return "ResourceImplementation";
    }

    @Override
    public Set<Resource> prerequisites() {
        return Collections.singleton(this);
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
