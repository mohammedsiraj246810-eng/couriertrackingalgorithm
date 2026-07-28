/**
 * Application entry point for the Courier Tracking System.
 */
package com.courier;

import com.courier.persistence.FileCourierRepository;
import com.courier.service.CourierService;
import com.courier.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.IOException;

/**
 * Boots the Swing application.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
                // Nimbus unavailable, use default.
            }

            try {
                CourierService service = new CourierService(new FileCourierRepository("data/couriers.csv"));
                MainFrame frame = new MainFrame(service);
                frame.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
