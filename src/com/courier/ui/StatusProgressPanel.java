/**
 * Visual step indicator for courier delivery status.
 */
package com.courier.ui;

import com.courier.model.CourierStatus;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * Draws a horizontal status progression.
 */
public class StatusProgressPanel extends JPanel {

    private CourierStatus status = CourierStatus.BOOKED;
    private static final List<CourierStatus> STAGES = List.of(CourierStatus.BOOKED, CourierStatus.PICKED_UP, CourierStatus.IN_TRANSIT, CourierStatus.OUT_FOR_DELIVERY, CourierStatus.DELIVERED);

    public StatusProgressPanel() {
        setPreferredSize(new Dimension(0, 120));
    }

    public void setStatus(CourierStatus status) {
        this.status = status;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int circleDiameter = 24;
        int stages = STAGES.size();
        int spacing = (width - circleDiameter) / (stages - 1);
        int y = getHeight() / 2 - circleDiameter / 2;

        int completedIndex = STAGES.indexOf(status);

        for (int i = 0; i < stages; i++) {
            int x = i * spacing;
            if (i > 0) {
                int prevX = (i - 1) * spacing + circleDiameter / 2;
                int curX = x + circleDiameter / 2;
                g2.setColor(i <= completedIndex ? Color.GREEN.darker() : Color.LIGHT_GRAY);
                g2.setStroke(new java.awt.BasicStroke(4f));
                g2.draw(new Line2D.Float(prevX, y + circleDiameter / 2, curX, y + circleDiameter / 2));
            }

            Color fill = i <= completedIndex ? STAGES.get(i).getColor() : Color.WHITE;
            Color border = i <= completedIndex ? STAGES.get(i).getColor().darker() : Color.GRAY;
            g2.setColor(fill);
            g2.fill(new Ellipse2D.Float(x, y, circleDiameter, circleDiameter));
            g2.setColor(border);
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.draw(new Ellipse2D.Float(x, y, circleDiameter, circleDiameter));

            String label = STAGES.get(i).getDisplayName();
            JLabel temp = new JLabel(label);
            Dimension labelSize = temp.getPreferredSize();
            g2.setColor(Color.BLACK);
            g2.drawString(label, x - (labelSize.width / 2) + circleDiameter / 2, y + circleDiameter + 20);
        }

        g2.dispose();
    }
}
