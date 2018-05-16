package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Loader;
import org.boudnik.ariadne.Resource;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Sergey Nuyanzin
 * @since 5/8/2018
 */
public class RIResource implements Resource<Collection<Device32ports>> {
    private String name;
    private Set<LocationResource<Device32ports>> locationResources = new HashSet<>();

    public RIResource addLocationCondition(Predicate<Device32ports> locationResource) {
        this.locationResources.add(new LocationResource<>(locationResource));
        return this;
    }

    public void build(Loader<Collection<Device32ports>> loader) {
        System.out.println("start build");
        Stream<Device32ports> stream = loader.getData().stream();
        System.out.println("start build");
        for (LocationResource r : prerequisites()) {
            stream = stream.filter(r.getPredicate());
        }
        stream.forEach(System.out::println);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String type() {
        return name;
    }

    @Override
    public Set<LocationResource<Device32ports>> prerequisites() {
        return locationResources;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public String toString() {
        return "RIResource{" +
                "locationResources=" + locationResources +
                '}';
    }
}
