/**
 * Panel for tracking a courier and updating its status.
 */
package com.courier.ui.panels;

import com.courier.model.Courier;
import com.courier.model.CourierStatus;
import com.courier.service.CourierService;
import com.courier.ui.BasePanel;
import com.courier.ui.StatusProgressPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

/**
 * Allows searching by tracking ID and displays courier details.
 */
public class TrackingPanel extends BasePanel {

    private final JTextField trackingIdField = new JTextField(20);
    private final JLabel detailsLabel = new JLabel("No parcel selected.");
    private final JComboBox<CourierStatus> statusCombo = new JComboBox<>(CourierStatus.values());
    private final JTextField locationField = new JTextField(20);
    private final JButton updateStatusButton = new JButton("Update Status");
    private final StatusProgressPanel statusProgressPanel = new StatusProgressPanel();
    private Courier currentCourier;
    private final Runnable onUpdate;

    public TrackingPanel(CourierService service, Runnable onUpdate) {
        super(service);
        this.onUpdate = onUpdate;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(0xF4F7FB));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(0xF4F7FB));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel header = new JLabel("Track a Parcel");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        gbc.gridwidth = 3;
        topPanel.add(header, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        topPanel.add(new JLabel("Tracking ID:"), gbc);
        gbc.gridx = 1;
        topPanel.add(trackingIdField, gbc);
        gbc.gridx = 2;
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0x0B79D0));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> handleSearch());
        topPanel.add(searchButton, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(statusProgressPanel, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD9E3EE), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        detailsPanel.add(new JLabel("Details:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        detailsPanel.add(detailsLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("New Status:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(statusCombo, gbc);
        gbc.gridx = 2;
        detailsPanel.add(new JLabel("Current Location:"), gbc);
        gbc.gridx = 3;
        detailsPanel.add(locationField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        updateStatusButton.setBackground(new Color(0x0B79D0));
        updateStatusButton.setForeground(Color.WHITE);
        updateStatusButton.setFocusPainted(false);
        updateStatusButton.addActionListener(e -> handleUpdateStatus());
        detailsPanel.add(updateStatusButton, gbc);

        add(detailsPanel, BorderLayout.SOUTH);
    }

    private void handleSearch() {
        String trackingId = trackingIdField.getText().trim();
        currentCourier = service.findByTrackingId(trackingId).orElse(null);
        if (currentCourier == null) {
            JOptionPane.showMessageDialog(this, "Courier not found for ID: " + trackingId, "Not Found", JOptionPane.WARNING_MESSAGE);
            detailsLabel.setText("No parcel selected.");
            statusProgressPanel.setStatus(CourierStatus.BOOKED);
            return;
        }
        detailsLabel.setText(String.format("%s → %s | Status: %s | Location: %s", currentCourier.getSenderName(), currentCourier.getReceiverName(), currentCourier.getStatus().getDisplayName(), currentCourier.getCurrentLocation()));
        statusProgressPanel.setStatus(currentCourier.getStatus());
        statusCombo.setSelectedItem(currentCourier.getStatus());
        locationField.setText(currentCourier.getCurrentLocation());
    }

    private void handleUpdateStatus() {
        if (currentCourier == null) {
            JOptionPane.showMessageDialog(this, "Search for a parcel first.", "Action Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CourierStatus status = (CourierStatus) statusCombo.getSelectedItem();
            String location = locationField.getText().trim();
            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter current location.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            service.updateStatus(currentCourier.getTrackingId(), status, location);
            JOptionPane.showMessageDialog(this, "Status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            handleSearch();
            onUpdate.run();
        } catch (IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void refreshData() {
        // No periodic refresh needed here.
    }
}
