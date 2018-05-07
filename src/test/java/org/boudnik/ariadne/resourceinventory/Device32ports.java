package org.boudnik.ariadne.resourceinventory;

import java.sql.Timestamp;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
public class Device32ports {
    private static final int NUMBER_OF_PORTS = 32;
    private ResourceInventoryLocation resourceInventoryLocation;
    private String physicalStatus;
    private String logicalStatus;
    private String softwareVersion;
    private Timestamp lastSoftwareUpdateDate;
    private final Port[] ports = new Port[NUMBER_OF_PORTS];

    public Device32ports(ResourceInventoryLocation resourceInventoryLocation) {
        this.resourceInventoryLocation = resourceInventoryLocation;
        for (int i = 0; i < NUMBER_OF_PORTS; i++) {
            ports[i] = new Port(i, this);
        }
    }

    public ResourceInventoryLocation getResourceInventoryLocation() {
        return resourceInventoryLocation;
    }

    public String getPhysicalStatus() {
        return physicalStatus;
    }

    public String getLogicalStatus() {
        return logicalStatus;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public Timestamp getLastSoftwareUpdateDate() {
        return lastSoftwareUpdateDate;
    }

    public Port getPort(int index) {
        if (index >= 0 && index < 32) {
            return ports[index];
        }
        throw new IllegalArgumentException("There are only 32 ports");
    }
}
