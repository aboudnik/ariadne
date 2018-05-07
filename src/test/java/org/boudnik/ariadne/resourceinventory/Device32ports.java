package org.boudnik.ariadne.resourceinventory;

import java.sql.Timestamp;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
public class Device32ports {
    private ResourceInventoryLocation resourceInventoryLocation;
    private String physicalStatus;
    private String logicalStatus;
    private String softwareVersion;
    private Timestamp lastSoftwareUpdateDate;
    private final Port[] ports = new Port[32];

    public Device32ports(ResourceInventoryLocation resourceInventoryLocation) {
        this.resourceInventoryLocation = resourceInventoryLocation;
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
        if(index >= 0 && index < 32)
        {
            return ports[index];
        }
        throw new IllegalArgumentException("There are only 32 ports");
    }
}
