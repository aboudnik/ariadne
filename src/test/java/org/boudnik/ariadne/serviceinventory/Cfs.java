package org.boudnik.ariadne.serviceinventory;

import java.util.Collection;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
//cfs == customer facing service
public class Cfs extends Service {
    private Collection<Cfs> secondLayerCfses; //could be null
    private Collection<Rfs> rfsCollection;

    public Cfs(String id, String name, String serviceSpecification) {
        super(id, name, serviceSpecification);
    }
}
