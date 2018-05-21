package org.boudnik.ariadne.resourceinventory;

import java.sql.Timestamp;

/**
 * @author Sergey Nuyanzin
 * @since 5/16/2018
 */
public abstract class Device {

    private RILocation resourceInventoryLocation;
    private DevicePhysicalStatus physicalStatus;
    private DeviceLogicalStatus logicalStatus;
    private String softwareVersion;
    private Timestamp lastSoftwareUpdateDate;

    public Device(RILocation resourceInventoryLocation) {
        this.resourceInventoryLocation = resourceInventoryLocation;
    }

    public RILocation getResourceInventoryLocation() {
        return resourceInventoryLocation;
    }

    public <T extends Device> T setPhysicalStatus(DevicePhysicalStatus physicalStatus) {
        this.physicalStatus = physicalStatus;
        return (T) this;
    }

    public <T extends Device> T setLogicalStatus(DeviceLogicalStatus logicalStatus) {
        this.logicalStatus = logicalStatus;
        return (T) this;
    }

    public <T extends Device> T setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
        return (T) this;
    }

    public <T extends Device> T setLastSoftwareUpdateDate(Timestamp lastSoftwareUpdateDate) {
        this.lastSoftwareUpdateDate = lastSoftwareUpdateDate;
        return (T) this;
    }

    public DevicePhysicalStatus getPhysicalStatus() {
        return physicalStatus;
    }

    public DeviceLogicalStatus getLogicalStatus() {
        return logicalStatus;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public Timestamp getLastSoftwareUpdateDate() {
        return lastSoftwareUpdateDate;
    }

    public abstract Port getPort(int index);


    public enum DeviceLogicalStatus {
        ACTIVE("In Service"),
        PASSIVE("Active"),
        DISABLED("Passive");

        private final String status;

        DeviceLogicalStatus(String status) {
            this.status = status;
        }
    }

    public enum DevicePhysicalStatus {
        CONNECTED("Connected"),
        NOT_CONNECTED("Not connected"),
        IN_SERVICE("In Service");

        private final String status;

        DevicePhysicalStatus(String status) {
            this.status = status;
        }
    }
}
