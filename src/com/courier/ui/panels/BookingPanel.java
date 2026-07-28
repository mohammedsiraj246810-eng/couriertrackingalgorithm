/**
 * Booking panel where users can create new courier records.
 */
package com.courier.ui.panels;

import com.courier.model.Priority;
import com.courier.service.CourierService;
import com.courier.ui.BasePanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

/**
 * Shows a booking form and validates entries before saving.
 */
public class BookingPanel extends BasePanel {

    private final JTextField senderNameField = new JTextField(20);
    private final JTextField senderPhoneField = new JTextField(20);
    private final JTextField receiverNameField = new JTextField(20);
    private final JTextField receiverPhoneField = new JTextField(20);
    private final JTextField addressField = new JTextField(20);
    private final JTextField weightField = new JTextField(10);
    private final JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
    private final JTextField codField = new JTextField(10);
    private final Runnable onSaved;

    public BookingPanel(CourierService service, Runnable onSaved) {
        super(service);
        this.onSaved = onSaved;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(0xF4F7FB));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel sectionTitle = new JLabel("Book a New Parcel");
        sectionTitle.setFont(sectionTitle.getFont().deriveFont(Font.BOLD, 22f));
        sectionTitle.setForeground(new Color(0x333333));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(sectionTitle, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel(""), gbc);
        gbc.gridy = 2;

        addLabel("Sender Name", 3, gbc);
        addField(senderNameField, 4, gbc);
        addLabel("Sender Phone", 5, gbc);
        addField(senderPhoneField, 6, gbc);
        addLabel("Receiver Name", 7, gbc);
        addField(receiverNameField, 8, gbc);
        addLabel("Receiver Phone", 9, gbc);
        addField(receiverPhoneField, 10, gbc);
        addLabel("Delivery Address", 11, gbc);
        addField(addressField, 12, gbc);
        addLabel("Weight (kg)", 13, gbc);
        addField(weightField, 14, gbc);
        addLabel("Priority", 15, gbc);
        addField(priorityCombo, 16, gbc);
        addLabel("COD Amount", 17, gbc);
        addField(codField, 18, gbc);

        JButton saveButton = new JButton("Book Parcel");
        saveButton.setBackground(new Color(0x0B79D0));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> handleBook());
        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.gridwidth = 2;
        add(saveButton, gbc);
    }

    private void addLabel(String text, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        add(label, gbc);
    }

    private void addField(java.awt.Component component, int row, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        add(component, gbc);
    }

    private void handleBook() {
        try {
            String senderName = senderNameField.getText();
            String senderPhone = senderPhoneField.getText();
            String receiverName = receiverNameField.getText();
            String receiverPhone = receiverPhoneField.getText();
            String address = addressField.getText();
            double weight = Double.parseDouble(weightField.getText().trim());
            Priority priority = (Priority) priorityCombo.getSelectedItem();
            double codAmount = Double.parseDouble(codField.getText().trim());
            service.bookCourier(senderName, senderPhone, receiverName, receiverPhone, address, weight, priority, codAmount);
            JOptionPane.showMessageDialog(this, "Parcel booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            onSaved.run();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for weight and COD.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        senderNameField.setText("");
        senderPhoneField.setText("");
        receiverNameField.setText("");
        receiverPhoneField.setText("");
        addressField.setText("");
        weightField.setText("");
        codField.setText("");
        priorityCombo.setSelectedIndex(0);
    }

    @Override
    public void refreshData() {
        // No dynamic state to refresh for the booking form.
    }
}
