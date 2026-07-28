/**
 * File-based repository implementation using CSV format.
 */
package com.courier.persistence;

import com.courier.model.Courier;
import com.courier.model.CourierStatus;
import com.courier.model.Priority;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads courier records from a local CSV file.
 */
public class FileCourierRepository implements CourierRepository {

    private static final String HEADER = "trackingId,senderName,senderPhone,receiverName,receiverPhone,address,weightKg,priority,codAmount,status,currentLocation";
    private final Path storagePath;

    public FileCourierRepository(String filePath) {
        this.storagePath = Paths.get(filePath);
    }

    @Override
    public List<Courier> loadAll() throws IOException {
        if (!Files.exists(storagePath)) {
            return new ArrayList<>();
        }

        List<Courier> couriers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(storagePath.toFile()))) {
            String line = reader.readLine();
            if (line == null || !line.equals(HEADER)) {
                return couriers;
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length != 11) {
                    continue;
                }
                Courier courier = parseCourier(parts);
                couriers.add(courier);
            }
        }
        return couriers;
    }

    @Override
    public void saveAll(List<Courier> couriers) throws IOException {
        File directory = storagePath.toFile().getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storagePath.toFile()))) {
            writer.write(HEADER);
            writer.newLine();
            for (Courier courier : couriers) {
                writer.write(formatCourier(courier));
                writer.newLine();
            }
        }
    }

    private Courier parseCourier(String[] parts) {
        String trackingId = parts[0];
        String senderName = parts[1];
        String senderPhone = parts[2];
        String receiverName = parts[3];
        String receiverPhone = parts[4];
        String address = parts[5];
        double weightKg = Double.parseDouble(parts[6]);
        Priority priority = Priority.valueOf(parts[7]);
        double codAmount = Double.parseDouble(parts[8]);
        CourierStatus status = CourierStatus.valueOf(parts[9]);
        String currentLocation = parts[10];

        return new Courier(trackingId, senderName, senderPhone, receiverName, receiverPhone, address, weightKg, priority, codAmount, status, currentLocation);
    }

    private String formatCourier(Courier courier) {
        return String.join(",",
                escape(courier.getTrackingId()),
                escape(courier.getSenderName()),
                escape(courier.getSenderPhone()),
                escape(courier.getReceiverName()),
                escape(courier.getReceiverPhone()),
                escape(courier.getAddress()),
                Double.toString(courier.getWeightKg()),
                courier.getPriority().name(),
                Double.toString(courier.getCodAmount()),
                courier.getStatus().name(),
                escape(courier.getCurrentLocation()));
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\r", " ").replace("\n", " ").replace(",", ";");
    }
}
