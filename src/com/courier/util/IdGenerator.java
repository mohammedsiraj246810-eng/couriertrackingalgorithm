/**
 * Generates incremental courier tracking identifiers.
 */
package com.courier.util;

/**
 * Simple ID generation following the CT#### format.
 */
public final class IdGenerator {

    private static int nextId = 1001;

    private IdGenerator() {
        // Utility class.
    }

    public static synchronized String nextTrackingId() {
        String id = String.format("CT%04d", nextId);
        nextId++;
        return id;
    }

    public static synchronized void ensureStartingId(int maxExistingId) {
        if (maxExistingId >= nextId) {
            nextId = maxExistingId + 1;
        }
    }
}
