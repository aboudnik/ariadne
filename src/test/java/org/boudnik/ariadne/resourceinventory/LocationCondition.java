package org.boudnik.ariadne.resourceinventory;

import org.boudnik.ariadne.Resource;

import java.util.Collections;
import java.util.Set;

/**
 * @author Sergey Nuyanzin
 * @since 5/8/2018
 */
public class LocationCondition {
    private String regionId;
    private String cityId;
    private String buildingId;
    private String rackId;
    private String deviceId;

    public LocationCondition() {

    }

    public String getRegionId() {
        return regionId;
    }

    public LocationCondition setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public String getCityId() {
        return cityId;
    }

    public LocationCondition setCityId(String cityId) {
        this.cityId = cityId;
        return this;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public LocationCondition setBuildingId(String buildingId) {
        this.buildingId = buildingId;
        return this;
    }

    public String getRackId() {
        return rackId;
    }

    public LocationCondition setRackId(String rackId) {
        this.rackId = rackId;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocationCondition setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    @Override
    public String toString() {
        return "LocationCondition{" +
                "regionId='" + regionId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", buildingId='" + buildingId + '\'' +
                ", rackId='" + rackId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

}
