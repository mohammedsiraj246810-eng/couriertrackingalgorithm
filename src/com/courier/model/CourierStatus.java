/**
 * Enum representing courier delivery statuses.
 */
package com.courier.model;

import java.awt.Color;

/**
 * Courier delivery status values with display names and color coding.
 */
public enum CourierStatus {
    BOOKED("Booked", new Color(0x2E7D32)),
    PICKED_UP("Picked Up", new Color(0x1565C0)),
    IN_TRANSIT("In Transit", new Color(0x0288D1)),
    OUT_FOR_DELIVERY("Out for Delivery", new Color(0xF9A825)),
    DELIVERED("Delivered", new Color(0x2E7D32));

    private final String displayName;
    private final Color color;

    CourierStatus(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
