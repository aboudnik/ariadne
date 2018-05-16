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
public class RIResource implements Resource<Collection<Device>> {
    private String name;
    private Set<RICondition<Device>> locationResources = new HashSet<>();

    public RIResource addCondition(Predicate<Device> locationResource) {
        this.locationResources.add(new RICondition<>(locationResource));
        return this;
    }

    public void build(Loader<Collection<Device>> loader) {
        Stream<Device> stream = loader.getData().stream();
        for (RICondition r : prerequisites()) {
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
    public Set<RICondition<Device>> prerequisites() {
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
