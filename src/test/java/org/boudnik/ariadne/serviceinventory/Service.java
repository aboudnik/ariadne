package org.boudnik.ariadne.serviceinventory;

import java.sql.Timestamp;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
public abstract class Service {
    private final String id;
    private final String name;
    private final String serviceSpecification;
    private String status;
    private Timestamp lastServiceUpdateDate;

    public Service(String id, String name, String serviceSpecification) {
        this.id = id;
        this.name = name;
        this.serviceSpecification = serviceSpecification;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Timestamp getLastServiceUpdateDate() {
        return lastServiceUpdateDate;
    }

    
}
