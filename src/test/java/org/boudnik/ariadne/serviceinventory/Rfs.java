package org.boudnik.ariadne.serviceinventory;

import java.util.Collection;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
//rfs == resource facing service
public class Rfs extends Service{
    private Collection<Rfs> secondLayerRfses; //could be null
    public Rfs(String id, String name, String serviceSpecification) {
        super(id, name, serviceSpecification);
    }
}
