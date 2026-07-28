/**
 * Base panel abstraction for shared UI behavior.
 */
package com.courier.ui;

import com.courier.service.CourierService;

import javax.swing.JPanel;

/**
 * Abstract base panel demonstrating inheritance and polymorphism.
 */
public abstract class BasePanel extends JPanel {

    protected final CourierService service;

    protected BasePanel(CourierService service) {
        this.service = service;
    }

    protected abstract void initializeComponents();

    public abstract void refreshData();
}
