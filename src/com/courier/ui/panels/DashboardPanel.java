/**
 * Dashboard UI showing summary statistics.
 */
package com.courier.ui.panels;

import com.courier.model.CourierStatus;
import com.courier.service.CourierService;
import com.courier.ui.BasePanel;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Dashboard view with summary cards for total parcels, status counts, and weights.
 */
public class DashboardPanel extends BasePanel {

    private final JLabel totalParcelsValue = new JLabel();
    private final JLabel bookedValue = new JLabel();
    private final JLabel pickedUpValue = new JLabel();
    private final JLabel inTransitValue = new JLabel();
    private final JLabel outForDeliveryValue = new JLabel();
    private final JLabel deliveredValue = new JLabel();
    private final JLabel totalWeightValue = new JLabel();
    private final JLabel averageWeightValue = new JLabel();
    private final JLabel codOutstandingValue = new JLabel();

    public DashboardPanel(CourierService service) {
        super(service);
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(0xF4F7FB));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        add(createCard("Total Parcels", totalParcelsValue, new Color(0x2196F3)), gbc, 0, 0, 2);
        add(createCard("Booked", bookedValue, new Color(0x26A69A)), gbc, 2, 0, 1);
        add(createCard("Picked Up", pickedUpValue, new Color(0x29B6F6)), gbc, 3, 0, 1);
        add(createCard("In Transit", inTransitValue, new Color(0x7E57C2)), gbc, 0, 1, 1);
        add(createCard("Out for Delivery", outForDeliveryValue, new Color(0xFFA726)), gbc, 1, 1, 1);
        add(createCard("Delivered", deliveredValue, new Color(0x66BB6A)), gbc, 2, 1, 1);
        add(createCard("Total Weight (kg)", totalWeightValue, new Color(0x8D6E63)), gbc, 3, 1, 1);
        add(createCard("Average Weight (kg)", averageWeightValue, new Color(0x5C6BC0)), gbc, 0, 2, 2);
        add(createCard("Total COD Outstanding", codOutstandingValue, new Color(0xD32F2F)), gbc, 2, 2, 2);
    }

    private DashboardCardPanel createCard(String title, JLabel valueLabel, Color accent) {
        DashboardCardPanel card = new DashboardCardPanel(title, valueLabel, accent);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        return card;
    }

    private void add(DashboardCardPanel card, GridBagConstraints gbc, int x, int y, int width) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        add(card, gbc);
    }

    @Override
    public void refreshData() {
        totalParcelsValue.setText(String.valueOf(service.getTotalParcels()));
        bookedValue.setText(String.valueOf(service.countByStatus(CourierStatus.BOOKED)));
        pickedUpValue.setText(String.valueOf(service.countByStatus(CourierStatus.PICKED_UP)));
        inTransitValue.setText(String.valueOf(service.countByStatus(CourierStatus.IN_TRANSIT)));
        outForDeliveryValue.setText(String.valueOf(service.countByStatus(CourierStatus.OUT_FOR_DELIVERY)));
        deliveredValue.setText(String.valueOf(service.countByStatus(CourierStatus.DELIVERED)));
        totalWeightValue.setText(String.format("%.2f", service.getTotalWeight()));
        averageWeightValue.setText(String.format("%.2f", service.getAverageWeight()));
        codOutstandingValue.setText(String.format("₹%.2f", service.getTotalCodOutstanding()));
    }

    private static class DashboardCardPanel extends JPanel {

        DashboardCardPanel(String title, JLabel valueLabel, Color accent) {
            setLayout(new java.awt.BorderLayout(10, 10));
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(240, 120));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xD9E3EE), 1),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(new Color(0x57606E));
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 15f));

            JLabel valueComponent = valueLabel != null ? valueLabel : new JLabel();
            valueComponent.setForeground(accent.darker());
            valueComponent.setFont(valueComponent.getFont().deriveFont(Font.BOLD, 32f));

            add(titleLabel, java.awt.BorderLayout.NORTH);
            add(valueComponent, java.awt.BorderLayout.CENTER);
        }
    }
}
