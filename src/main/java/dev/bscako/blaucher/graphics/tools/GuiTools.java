/**
 *
 */
package dev.bscako.blaucher.graphics.tools;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Fonctions utilitaire des interface graphique
 *
 * @author BSCAKO
 *
 */
public class GuiTools {

    public static void setSize(JDialog jDialog, int percent) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int w = screenSize.width - ((screenSize.width * percent) / 100);
        final int h = screenSize.height - ((screenSize.height * percent) / 100);
        jDialog.setSize(new Dimension(w, h));
    }

    /**
     * Efface le contnu d'un combobox
     *
     * @param jComboBox
     * @param defaultString
     */
    public static void clearComboBox(JComboBox<String> jComboBox, String defaultString) {
        jComboBox.setModel(new DefaultComboBoxModel<>());
        if (defaultString != null) {
            jComboBox.setModel(new DefaultComboBoxModel<>(new String[]{defaultString}));
        }
    }

    /**
     * Obtient le contenu d'un jTextField initialise avec un placeholder
     *
     * @param jTextField
     * @param defaultText
     * @return
     */
    public static String getJTextFieldString(final JTextField jTextField, String defaultText) {
        if ((jTextField.getText() == null) || (jTextField.getText().trim().isEmpty())
                || (jTextField.getText().equalsIgnoreCase(defaultText))) {
            return null;
        }
        return jTextField.getText().trim();
    }

    /**
     * mise en place d'un placeholdr pour un JTextField
     *
     * @param jTextField
     * @param text
     */
    public static void setNewJTextFieldPlaceHolder(final JTextField jTextField, String text) {
        jTextField.setForeground(Color.GRAY);
        jTextField.setText(text);
    }

    /**
     * Mise en place d'un place holder
     *
     * @param jTextField
     * @param text
     */
    public static void setJTextFieldPlaceHolder(final JTextField jTextField, String text) {
        jTextField.setForeground(Color.GRAY);
        jTextField.setText(text);
        jTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent arg0) {
                if (jTextField.getText().isEmpty()) {
                    jTextField.setForeground(Color.GRAY);
                    jTextField.setText(text);
                }
            }

            @Override
            public void focusGained(FocusEvent arg0) {
                if (jTextField.getText().equals(text)) {
                    jTextField.setText(null);
                    jTextField.setForeground(Color.BLACK);
                }
            }
        });
    }

    /**
     * mise en place du design d'une interface graphique
     *
     * @param name
     */
    public static void setInterfaceDesign(String name) {

        try {
            LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
            for (LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {
                if (name.equals(installedLookAndFeel.getName())) {
                    UIManager.setLookAndFeel(installedLookAndFeel.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiTools.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public static class JDialogDragListener extends MouseAdapter {

        private final JDialog frame;
        private Point mouseDownCompCoords = null;

        public JDialogDragListener(JDialog frame) {
            this.frame = frame;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.mouseDownCompCoords = null;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.mouseDownCompCoords = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            this.frame.setLocation(currCoords.x - this.mouseDownCompCoords.x,
                    currCoords.y - this.mouseDownCompCoords.y);
        }
    }

}
