package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Loader;
import org.boudnik.ariadne.Resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Sergey Nuyanzin
 * @since 5/8/2018
 */
public class RIResource implements Resource<Collection<Device>> {
    private String name;
    private Set<RICondition> locationResources = new HashSet<>();

    public RIResource addCondition(Function<Device, ?> function2getValue, Object value) {
        try {
            this.locationResources.add(new RICondition(function2getValue, value));
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void build(Loader<Collection<Device>> loader) {
        Stream<Device> stream = loader.getData().stream();
        for (RICondition r : prerequisites()) {
            stream = stream.filter(device -> Objects.equals(r.getValue(), r.getFunction2getValue().apply(device)));
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
    public Set<RICondition> prerequisites() {
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
