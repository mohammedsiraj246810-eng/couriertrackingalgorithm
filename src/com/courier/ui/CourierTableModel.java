/**
 * Table model for courier records in the all-records table.
 */
package com.courier.ui;

import com.courier.model.Courier;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses ArrayList internally to represent rows of courier data.
 */
public class CourierTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"ID", "Sender", "Receiver", "Status", "Weight (kg)", "Priority"};
    private List<Courier> couriers = new ArrayList<>();

    public void setCouriers(List<Courier> couriers) {
        this.couriers = new ArrayList<>(couriers);
        fireTableDataChanged();
    }

    public Courier getCourierAt(int rowIndex) {
        return couriers.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return couriers.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Courier courier = couriers.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> courier.getTrackingId();
            case 1 -> courier.getSenderName();
            case 2 -> courier.getReceiverName();
            case 3 -> courier.getStatus();
            case 4 -> courier.getWeightKg();
            case 5 -> courier.getPriority();
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 4 -> Double.class;
            case 3 -> courierClass();
            default -> String.class;
        };
    }

    private Class<?> courierClass() {
        return com.courier.model.CourierStatus.class;
    }
}
