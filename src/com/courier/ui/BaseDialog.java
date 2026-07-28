/**
 * Base dialog abstract class for shared dialog setup.
 */
package com.courier.ui;

import javax.swing.JDialog;
import java.awt.Frame;

/**
 * Abstract dialog demonstrating inheritance and polymorphism.
 */
public abstract class BaseDialog extends JDialog {

    protected BaseDialog(Frame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected abstract void initializeComponents();
}
