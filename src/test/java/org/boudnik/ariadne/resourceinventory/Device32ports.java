package org.boudnik.ariadne.resourceinventory;

/**
 * @author Sergey Nuyanzin
 * @since 5/7/2018
 */
public class Device32ports extends Device {
    private static final int NUMBER_OF_PORTS = 32;
    private final Port[] ports = new Port[NUMBER_OF_PORTS];

    public Device32ports(RILocation resourceInventoryLocation) {
        super(resourceInventoryLocation);
        for (int i = 0; i < NUMBER_OF_PORTS; i++) {
            ports[i] = new Port(i, this);
        }
    }

    public Port getPort(int index) {
        if (index >= 0 && index < 32) {
            return ports[index];
        }
        throw new IllegalArgumentException("There are only 32 ports");
    }
}
