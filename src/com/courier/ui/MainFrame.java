/**
 * Main application window for the courier tracking system.
 */
package com.courier.ui;

import com.courier.service.CourierService;
import com.courier.ui.panels.AllRecordsPanel;
import com.courier.ui.panels.BookingPanel;
import com.courier.ui.panels.DashboardPanel;
import com.courier.ui.panels.TrackingPanel;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Creates the main window and navigation tabs.
 */
public class MainFrame extends JFrame {

    private final DashboardPanel dashboardPanel;
    private final BookingPanel bookingPanel;
    private final TrackingPanel trackingPanel;
    private final AllRecordsPanel allRecordsPanel;

    public MainFrame(CourierService service) {
        super("Courier Tracking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 650));
        setMinimumSize(new Dimension(900, 600));

        dashboardPanel = new DashboardPanel(service);
        bookingPanel = new BookingPanel(service, this::refreshAllPanels);
        trackingPanel = new TrackingPanel(service, this::refreshAllPanels);
        allRecordsPanel = new AllRecordsPanel(service, this::refreshAllPanels);

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xD0D7DE)),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)));
        JLabel title = new JLabel("Courier Tracking System");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        JLabel subtitle = new JLabel("Fast parcel booking, tracking and delivery status management.");
        subtitle.setForeground(new Color(0x555555));
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.setBackground(new Color(0xF8FAFC));
        tabs.addTab("Dashboard", dashboardPanel);
        tabs.addTab("Book Parcel", bookingPanel);
        tabs.addTab("Track Parcel", trackingPanel);
        tabs.addTab("All Records", allRecordsPanel);

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        refreshAllPanels();
        pack();
        setLocationRelativeTo(null);
    }

    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        bookingPanel.refreshData();
        trackingPanel.refreshData();
        allRecordsPanel.refreshData();
    }
}
