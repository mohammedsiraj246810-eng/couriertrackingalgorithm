/**
 * Domain model for a courier parcel. This class uses encapsulation to keep fields private
 * and exposes getters/setters for permitted updates.
 */
package com.courier.model;

import java.time.LocalDateTime;

/**
 * Represents a courier parcel in the system.
 */
public class Courier {

    private final String trackingId;
    private final String senderName;
    private final String senderPhone;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private double weightKg;
    private final Priority priority;
    private final double codAmount;
    private CourierStatus status;
    private String currentLocation;
    private final LocalDateTime bookedAt;

    /**
     * Constructs a new Courier record.
     *
     * @param trackingId    generated tracking identifier
     * @param senderName    sender name
     * @param senderPhone   sender phone
     * @param receiverName  receiver name
     * @param receiverPhone receiver phone
     * @param address       delivery address
     * @param weightKg      parcel weight in kilograms
     * @param priority      delivery priority
     * @param codAmount     cash on delivery amount
     * @param status        current status
     * @param currentLocation current location text
     */
    public Courier(String trackingId,
                   String senderName,
                   String senderPhone,
                   String receiverName,
                   String receiverPhone,
                   String address,
                   double weightKg,
                   Priority priority,
                   double codAmount,
                   CourierStatus status,
                   String currentLocation) {
        this.trackingId = trackingId;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.address = address;
        this.weightKg = weightKg;
        this.priority = priority;
        this.codAmount = codAmount;
        this.status = status;
        this.currentLocation = currentLocation;
        this.bookedAt = LocalDateTime.now();
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public Priority getPriority() {
        return priority;
    }

    public double getCodAmount() {
        return codAmount;
    }

    public CourierStatus getStatus() {
        return status;
    }

    public void setStatus(CourierStatus status) {
        this.status = status;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    @Override
    public String toString() {
        return trackingId + " - " + receiverName + " (" + status + ")";
    }
}
