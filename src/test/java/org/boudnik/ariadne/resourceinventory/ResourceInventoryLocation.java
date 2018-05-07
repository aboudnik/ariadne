package org.boudnik.ariadne.resourceinventory;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
public class ResourceInventoryLocation {
    private final String regionId;
    private final String cityId;
    private final String buildingId;
    private final String rackId;
    private final String deviceId;

    public ResourceInventoryLocation(String regionId, String cityId, String buildingId, String rackId, String deviceId) {
        this.regionId = regionId;
        this.cityId = cityId;
        this.buildingId = buildingId;
        this.rackId = rackId;
        this.deviceId = deviceId;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public String getRackId() {
        return rackId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
