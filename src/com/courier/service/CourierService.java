/**
 * Business logic layer for courier operations.
 */
package com.courier.service;

import com.courier.model.Courier;
import com.courier.model.CourierStatus;
import com.courier.model.Priority;
import com.courier.persistence.CourierRepository;
import com.courier.util.IdGenerator;
import com.courier.util.InputValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Courier service manages parcel records and applies business rules.
 */
public class CourierService {

    private final CourierRepository repository;
    private final List<Courier> couriers;

    /**
     * Creates a new courier service backed by the given repository.
     *
     * @param repository persistence implementation
     * @throws IOException when loading existing data fails
     */
    public CourierService(CourierRepository repository) throws IOException {
        this.repository = repository;
        this.couriers = new ArrayList<>(repository.loadAll());
        ensureIdGenerator();
        if (couriers.isEmpty()) {
            loadDemoData();
            saveAll();
        }
    }

    private void ensureIdGenerator() {
        int maxId = couriers.stream()
                .map(Courier::getTrackingId)
                .map(id -> id.substring(2))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(1000);
        IdGenerator.ensureStartingId(maxId);
    }

    /**
     * Books a new courier parcel and persists the record.
     *
     * @param senderName sender name
     * @param senderPhone sender phone
     * @param receiverName receiver name
     * @param receiverPhone receiver phone
     * @param address delivery address
     * @param weightKg parcel weight in kg
     * @param priority delivery priority
     * @param codAmount COD amount
     * @return created courier record
     * @throws IOException when persistence fails
     */
    public Courier bookCourier(String senderName,
                               String senderPhone,
                               String receiverName,
                               String receiverPhone,
                               String address,
                               double weightKg,
                               Priority priority,
                               double codAmount) throws IOException {
        validateBooking(senderName, senderPhone, receiverName, receiverPhone, address, weightKg, codAmount);
        String trackingId = IdGenerator.nextTrackingId();
        Courier courier = new Courier(trackingId, senderName, senderPhone, receiverName, receiverPhone, address, weightKg, priority, codAmount, CourierStatus.BOOKED, "Warehouse");
        couriers.add(courier);
        saveAll();
        return courier;
    }

    /**
     * Retrieves all courier records currently in memory.
     *
     * @return list of couriers
     */
    public List<Courier> getAllCouriers() {
        return new ArrayList<>(couriers);
    }

    /**
     * Finds a courier record by tracking ID.
     *
     * @param trackingId the courier tracking identifier
     * @return optional courier record
     */
    public Optional<Courier> findByTrackingId(String trackingId) {
        return couriers.stream()
                .filter(c -> c.getTrackingId().equalsIgnoreCase(trackingId))
                .findFirst();
    }

    /**
     * Searches parcels by sender name using a case-insensitive partial match.
     *
     * @param senderName partial or full sender name
     * @return matching courier records
     */
    public List<Courier> searchBySender(String senderName) {
        String query = senderName == null ? "" : senderName.trim().toLowerCase();
        return couriers.stream()
                .filter(c -> c.getSenderName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    /**
     * Searches parcels by receiver name using a case-insensitive partial match.
     *
     * @param receiverName partial or full receiver name
     * @return matching courier records
     */
    public List<Courier> searchByReceiver(String receiverName) {
        String query = receiverName == null ? "" : receiverName.trim().toLowerCase();
        return couriers.stream()
                .filter(c -> c.getReceiverName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    /**
     * Filters parcels by courier status.
     *
     * @param status courier status to filter by
     * @return matching courier records
     */
    public List<Courier> filterByStatus(CourierStatus status) {
        return couriers.stream()
                .filter(c -> c.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Sorts parcels by weight.
     *
     * @param ascending true for ascending order, false for descending
     * @return sorted courier records
     */
    public List<Courier> sortByWeight(boolean ascending) {
        Comparator<Courier> comparator = Comparator.comparingDouble(Courier::getWeightKg);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return couriers.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * Updates the status and current location for a parcel.
     *
     * @param trackingId parcel tracking ID
     * @param status new status
     * @param location current location text
     * @return updated courier record
     * @throws IOException when persistence fails
     */
    public Courier updateStatus(String trackingId, CourierStatus status, String location) throws IOException {
        Courier courier = findByTrackingId(trackingId).orElseThrow(() -> new IllegalArgumentException("Courier not found: " + trackingId));
        courier.setStatus(status);
        courier.setCurrentLocation(location);
        saveAll();
        return courier;
    }

    /**
     * Edits the mutable fields of a parcel record.
     *
     * @param trackingId parcel tracking ID
     * @param receiverName receiver name
     * @param receiverPhone receiver phone
     * @param address delivery address
     * @param weightKg weight in kilograms
     * @return updated courier record
     * @throws IOException when persistence fails
     */
    public Courier editCourier(String trackingId,
                               String receiverName,
                               String receiverPhone,
                               String address,
                               double weightKg) throws IOException {
        validateEdit(receiverName, receiverPhone, address, weightKg);
        Courier courier = findByTrackingId(trackingId).orElseThrow(() -> new IllegalArgumentException("Courier not found: " + trackingId));
        courier.setReceiverName(receiverName);
        courier.setReceiverPhone(receiverPhone);
        courier.setAddress(address);
        courier.setWeightKg(weightKg);
        saveAll();
        return courier;
    }

    /**
     * Deletes a parcel record.
     *
     * @param trackingId parcel tracking ID
     * @throws IOException when persistence fails
     */
    public void deleteCourier(String trackingId) throws IOException {
        Courier courier = findByTrackingId(trackingId).orElseThrow(() -> new IllegalArgumentException("Courier not found: " + trackingId));
        couriers.remove(courier);
        saveAll();
    }

    public int getTotalParcels() {
        return couriers.size();
    }

    public long countByStatus(CourierStatus status) {
        return couriers.stream().filter(c -> c.getStatus() == status).count();
    }

    public double getTotalWeight() {
        return couriers.stream().mapToDouble(Courier::getWeightKg).sum();
    }

    public double getAverageWeight() {
        return couriers.isEmpty() ? 0 : getTotalWeight() / couriers.size();
    }

    public double getTotalCodOutstanding() {
        return couriers.stream().mapToDouble(Courier::getCodAmount).sum();
    }

    private void validateBooking(String senderName,
                                 String senderPhone,
                                 String receiverName,
                                 String receiverPhone,
                                 String address,
                                 double weightKg,
                                 double codAmount) {
        InputValidator.requireNonEmpty(senderName, "Sender name");
        InputValidator.requireNonEmpty(senderPhone, "Sender phone");
        InputValidator.requireNonEmpty(receiverName, "Receiver name");
        InputValidator.requireNonEmpty(receiverPhone, "Receiver phone");
        InputValidator.requireNonEmpty(address, "Address");
        InputValidator.requirePositive(weightKg, "Weight");
        InputValidator.requireNonNegative(codAmount, "COD amount");
    }

    private void validateEdit(String receiverName,
                              String receiverPhone,
                              String address,
                              double weightKg) {
        InputValidator.requireNonEmpty(receiverName, "Receiver name");
        InputValidator.requireNonEmpty(receiverPhone, "Receiver phone");
        InputValidator.requireNonEmpty(address, "Address");
        InputValidator.requirePositive(weightKg, "Weight");
    }

    private void saveAll() throws IOException {
        repository.saveAll(couriers);
    }

    private void loadDemoData() {
        couriers.add(new Courier(IdGenerator.nextTrackingId(), "Riya Sharma", "9876543210", "Amit Verma", "9123456780", "42 Green Street, Pune", 2.5, Priority.NORMAL, 0, CourierStatus.BOOKED, "Warehouse"));
        couriers.add(new Courier(IdGenerator.nextTrackingId(), "Aakash Singh", "9988776655", "Priya Nair", "9876512340", "18 Park Avenue, Mumbai", 1.2, Priority.EXPRESS, 100, CourierStatus.IN_TRANSIT, "Mumbai Hub"));
        couriers.add(new Courier(IdGenerator.nextTrackingId(), "Meera Patel", "9123401234", "Rahul Joshi", "9812345670", "12 Lakeside Road, Delhi", 4.0, Priority.NORMAL, 250, CourierStatus.OUT_FOR_DELIVERY, "Delhi Distribution"));
        couriers.add(new Courier(IdGenerator.nextTrackingId(), "Sahil Reddy", "9012345678", "Nisha Gupta", "9823456710", "77 Flower Road, Bengaluru", 3.8, Priority.EXPRESS, 50, CourierStatus.PICKED_UP, "Bengaluru Pickup"));
        couriers.add(new Courier(IdGenerator.nextTrackingId(), "Tanvi Joshi", "9567123450", "Vikram Rao", "9998765432", "29 Hillview Lane, Hyderabad", 1.0, Priority.NORMAL, 0, CourierStatus.DELIVERED, "Delivered"));
    }
}
