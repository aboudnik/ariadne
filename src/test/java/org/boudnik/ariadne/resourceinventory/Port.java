package org.boudnik.ariadne.resourceinventory;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
public class Port {
    private final int portNumber;
    private final Device32ports device;
    private String status;

    public Port(int portNumber, Device32ports device) {
        this.portNumber = portNumber;
        this.device = device;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public Device32ports getDevice() {
        return device;
    }

    public String getStatus() {
        return status;
    }
}
