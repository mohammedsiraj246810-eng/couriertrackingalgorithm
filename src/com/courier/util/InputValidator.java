/**
 * Utility class for validating courier form inputs.
 */
package com.courier.util;

/**
 * Performs reusable validation checks for the courier tracking system.
 */
public final class InputValidator {

    private InputValidator() {
        // Utility class; prevent instantiation.
    }

    /**
     * Ensures a string is not null or blank.
     *
     * @param value the value to validate
     * @param fieldName label for exception message
     */
    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }

    /**
     * Ensures a numeric value is greater than zero.
     *
     * @param value the numeric value
     * @param fieldName label for exception message
     */
    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero.");
        }
    }

    /**
     * Ensures a numeric value is zero or greater.
     *
     * @param value the numeric value
     * @param fieldName label for exception message
     */
    public static void requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
    }

    /**
     * Checks whether a tracking ID matches the CT#### format.
     *
     * @param trackingId the ID to validate
     * @return true when ID is valid
     */
    public static boolean isValidTrackingId(String trackingId) {
        return trackingId != null && trackingId.matches("CT\\d{4}");
    }
}
