/**
 * Displays all courier records with search, sort, and filter controls.
 */
package com.courier.ui.panels;

import com.courier.model.Courier;
import com.courier.model.CourierStatus;
import com.courier.service.CourierService;
import com.courier.ui.BasePanel;
import com.courier.ui.CourierTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

/**
 * Shows courier records and allows editing, deleting, searching, filtering, and sorting.
 */
public class AllRecordsPanel extends BasePanel {

    private final CourierTableModel tableModel = new CourierTableModel();
    private final JTable table = new JTable(tableModel);
    private final JTextField senderSearchField = new JTextField(12);
    private final JTextField receiverSearchField = new JTextField(12);
    private final JTextField locationSearchField = new JTextField(12);
    private final JComboBox<CourierStatus> statusFilterCombo = new JComboBox<>();
    private final JComboBox<String> priorityFilterCombo = new JComboBox<>();
    private final JComboBox<String> sortCombo = new JComboBox<>(new String[] {"Weight Ascending", "Weight Descending"});
    private final JLabel resultCountLabel = new JLabel();
    private final Runnable onUpdate;

    public AllRecordsPanel(CourierService service, Runnable onUpdate) {
        super(service);
        this.onUpdate = onUpdate;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(0xF4F7FB));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel toolbar = new JPanel(new GridBagLayout());
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD9E3EE), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        toolbar.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        statusFilterCombo.addItem(null);
        for (CourierStatus status : CourierStatus.values()) {
            statusFilterCombo.addItem(status);
        }
        priorityFilterCombo.addItem("Any Priority");
        for (String priority : new String[] {"Normal", "Express"}) {
            priorityFilterCombo.addItem(priority);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel header = new JLabel("All Parcels");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        gbc.gridwidth = 15;
        toolbar.add(header, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        toolbar.add(new JLabel("Sender:"), gbc);
        gbc.gridx = 1;
        toolbar.add(senderSearchField, gbc);
        gbc.gridx = 2;
        toolbar.add(new JLabel("Receiver:"), gbc);
        gbc.gridx = 3;
        toolbar.add(receiverSearchField, gbc);
        gbc.gridx = 4;
        toolbar.add(new JLabel("Location:"), gbc);
        gbc.gridx = 5;
        toolbar.add(locationSearchField, gbc);
        gbc.gridx = 6;
        toolbar.add(new JLabel("Status:"), gbc);
        gbc.gridx = 7;
        toolbar.add(statusFilterCombo, gbc);
        gbc.gridx = 8;
        toolbar.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 9;
        toolbar.add(priorityFilterCombo, gbc);
        gbc.gridx = 10;
        toolbar.add(new JLabel("Sort:"), gbc);
        gbc.gridx = 11;
        toolbar.add(sortCombo, gbc);
        gbc.gridx = 12;
        JButton refreshButton = new JButton("Apply");
        refreshButton.setBackground(new Color(0x0B79D0));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshData());
        toolbar.add(refreshButton, gbc);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearFilters());
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> handleEdit());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDelete());
        gbc.gridx = 13;
        toolbar.add(clearButton, gbc);
        gbc.gridx = 14;
        toolbar.add(editButton, gbc);
        gbc.gridx = 15;
        toolbar.add(deleteButton, gbc);

        add(toolbar, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.setGridColor(new Color(0xE7EDF3));
        table.setShowGrid(true);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleEdit();
                }
            }
        });
        table.setDefaultRenderer(Object.class, new StatusColorRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        resultCountLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(resultCountLabel, BorderLayout.SOUTH);
        refreshData();
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a parcel row to edit.", "Select Row", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Courier courier = tableModel.getCourierAt(row);
        java.awt.Frame owner = null;
        java.awt.Container ancestor = getTopLevelAncestor();
        if (ancestor instanceof java.awt.Frame frame) {
            owner = frame;
        }
        EditDialog dialog = new EditDialog(courier, service, owner, onUpdate);
        dialog.setVisible(true);
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a parcel row to delete.", "Select Row", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Courier courier = tableModel.getCourierAt(row);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete parcel " + courier.getTrackingId() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteCourier(courier.getTrackingId());
                JOptionPane.showMessageDialog(this, "Parcel deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                onUpdate.run();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFilters() {
        senderSearchField.setText("");
        receiverSearchField.setText("");
        locationSearchField.setText("");
        statusFilterCombo.setSelectedItem(null);
        priorityFilterCombo.setSelectedIndex(0);
        sortCombo.setSelectedIndex(0);
        refreshData();
    }

    @Override
    public void refreshData() {
        List<Courier> results = service.getAllCouriers();
        String senderQuery = senderSearchField.getText().trim();
        String receiverQuery = receiverSearchField.getText().trim();
        if (!senderQuery.isEmpty()) {
            results = service.searchBySender(senderQuery);
        }
        if (!receiverQuery.isEmpty()) {
            results = results.stream().filter(c -> c.getReceiverName().toLowerCase().contains(receiverQuery.toLowerCase())).toList();
        }
        CourierStatus status = (CourierStatus) statusFilterCombo.getSelectedItem();
        if (status != null) {
            results = results.stream().filter(c -> c.getStatus() == status).toList();
        }
        boolean ascending = sortCombo.getSelectedIndex() == 0;
        results = results.stream().sorted((a, b) -> {
            int compare = Double.compare(a.getWeightKg(), b.getWeightKg());
            return ascending ? compare : -compare;
        }).toList();
        tableModel.setCouriers(results);
        resultCountLabel.setText("Showing " + results.size() + " parcel(s)");
    }

    private static class StatusColorRenderer extends DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof CourierStatus status) {
                comp.setForeground(status.getColor());
            } else {
                comp.setForeground(Color.BLACK);
            }
            return comp;
        }
    }
}
