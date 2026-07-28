/**
 * Enum representing delivery priority levels.
 */
package com.courier.model;

/**
 * Delivery priority for a courier parcel.
 */
public enum Priority {
    NORMAL("Normal"),
    EXPRESS("Express");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
