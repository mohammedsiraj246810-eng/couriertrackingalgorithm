/**
 * Dialog for editing selected courier receiver details.
 */
package com.courier.ui.panels;

import com.courier.model.Courier;
import com.courier.service.CourierService;
import com.courier.ui.BaseDialog;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

/**
 * Dialog for editing parcel fields allowed by the business logic.
 */
public class EditDialog extends BaseDialog {

    private final Courier courier;
    private final CourierService service;
    private final Runnable onSaved;
    private final JTextField receiverNameField = new JTextField(20);
    private final JTextField receiverPhoneField = new JTextField(20);
    private final JTextField addressField = new JTextField(20);
    private final JTextField weightField = new JTextField(10);

    public EditDialog(Courier courier, CourierService service, Frame owner, Runnable onSaved) {
        super(owner, "Edit Parcel " + courier.getTrackingId());
        this.courier = courier;
        this.service = service;
        this.onSaved = onSaved;
        initializeComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    @Override
    protected void initializeComponents() {
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        addLabel(content, "Receiver Name", 0, gbc);
        addField(content, receiverNameField, 1, gbc);
        addLabel(content, "Receiver Phone", 2, gbc);
        addField(content, receiverPhoneField, 3, gbc);
        addLabel(content, "Address", 4, gbc);
        addField(content, addressField, 5, gbc);
        addLabel(content, "Weight (kg)", 6, gbc);
        addField(content, weightField, 7, gbc);

        receiverNameField.setText(courier.getReceiverName());
        receiverPhoneField.setText(courier.getReceiverPhone());
        addressField.setText(courier.getAddress());
        weightField.setText(String.valueOf(courier.getWeightKg()));

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> handleSave());
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        content.add(saveButton, gbc);

        setContentPane(content);
    }

    private void addLabel(JPanel content, String text, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        content.add(new JLabel(text), gbc);
    }

    private void addField(JPanel content, JTextField field, int row, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        content.add(field, gbc);
    }

    private void handleSave() {
        try {
            String receiverName = receiverNameField.getText().trim();
            String receiverPhone = receiverPhoneField.getText().trim();
            String address = addressField.getText().trim();
            double weight = Double.parseDouble(weightField.getText().trim());
            service.editCourier(courier.getTrackingId(), receiverName, receiverPhone, address, weight);
            JOptionPane.showMessageDialog(this, "Parcel updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            onSaved.run();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Provide a valid numeric weight.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
