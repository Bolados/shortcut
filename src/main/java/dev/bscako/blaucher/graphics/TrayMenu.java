package dev.bscako.blaucher.graphics;

import dev.bscako.blaucher.configuration.ApplicationProperties;
import dev.bscako.blaucher.configuration.NativeHookConfig;
import dev.bscako.blaucher.graphics.tools.CustomSwingUI;
import dev.bscako.blaucher.models.services.ApplicationService;
import dev.bscako.blaucher.models.services.CategoryService;
import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author BSCAKO
 *
 */

@Component
public class TrayMenu implements NativeMouseListener {

    private static final long serialVersionUID = 619291297778454821L;

    private SystemTray systemTray;

    @Autowired
    private JDialogSettings jDialogSettings;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private NativeHookConfig nativeHookConfig;

    @Autowired
    private ApplicationProperties applicationProperties;

    private InputStream systemTrayIcon;

    @Autowired
    public TrayMenu(ApplicationProperties applicationProperties, ResourceLoader resourceLoader,
                    NativeHookConfig nativeHookConfig) {
        SystemTray.DEBUG = true; // for test apps, we always want to run in
        // debug mode
        // CacheUtil.clear(); // for test apps, make sure the cache is always
        // reset. You should never do this in production.
        SystemTray.SWING_UI = new CustomSwingUI();

        this.systemTray = SystemTray.get();
        if (this.systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }
        this.loadResources(resourceLoader);

        if (this.systemTrayIcon != null) {
            this.systemTray.setImage(this.systemTrayIcon);
        }

        this.systemTray.setTooltip(applicationProperties.getName());


        Menu menu = this.systemTray.getMenu();

        MenuItem itemSettings = new MenuItem("Settings",
                e -> TrayMenu.this.settingsActionPerformed(e));
        MenuItem itemAbout = new MenuItem("About",
                e -> aboutActionPerformed(e));
        MenuItem itemManual = new MenuItem("Manual",
                e -> manualActionPerformed(e));
        MenuItem itemExit = new MenuItem("Quit", e -> this.exitActionPerformed(e));

        // MenuItem itemSettings = new MenuItem("Settings", BLAUCHER_ITEM_ICON,
        // (ActionListener) e -> TrayMenu.this.settingsActionPerformed(e));
        // MenuItem itemAbout = new MenuItem("About", BLAUCHER_ITEM_ICON,
        // (ActionListener) e -> TrayMenu.this.settingsActionPerformed(e));
        // MenuItem itemManual = new MenuItem("Manual", BLAUCHER_ITEM_ICON,
        // (ActionListener) e -> TrayMenu.this.settingsActionPerformed(e));
        // MenuItem itemExit = new MenuItem("Quit", e ->
        // this.exitActionPerformed(e));

        menu.add(itemSettings);
        menu.add(new JSeparator());
        menu.add(itemAbout);
        menu.add(itemManual);
        menu.add(new JSeparator());
        menu.add(itemExit).setShortcut('q');

        nativeHookConfig.startNativeHook(this);
    }

    public static void aboutActionPerformed(ActionEvent e) {

    }

    public static void manualActionPerformed(ActionEvent e) {

    }

    /**
     * @return the systemTray
     */
    public SystemTray getSystemTray() {
        return this.systemTray;
    }

    protected void loadResources(ResourceLoader resourceLoader) {
        try {
            this.systemTrayIcon = resourceLoader.getResource("classpath:images/blauchericon.png").getInputStream();
        } catch (IOException e) {
            this.systemTrayIcon = null;
        }
    }

    protected void settingsActionPerformed(ActionEvent e) {
        if (!this.jDialogSettings.isVisible()) {
            this.jDialogSettings.setVisible(true);
        }
        this.jDialogSettings.toFront();
    }

    protected void exitActionPerformed(ActionEvent e) {
        int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?",
                this.applicationProperties.getName(), JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            this.nativeHookConfig.cleanNativeHook();
            this.systemTray.shutdown();
            System.runFinalization();
            System.exit(0);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.jnativehook.mouse.NativeMouseListener#nativeMouseClicked(org.
     * jnativehook.mouse.NativeMouseEvent)
     */
    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.jnativehook.mouse.NativeMouseListener#nativeMousePressed(org.
     * jnativehook.mouse.NativeMouseEvent)
     */
    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if (e.getButton() == NativeMouseEvent.BUTTON2) {
            try {
                Robot bot = new Robot();
                bot.mousePress(InputEvent.BUTTON1_MASK);
                bot.mouseRelease(InputEvent.BUTTON1_MASK);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
            if (e.getY() == 0) {
                System.out.println("Top of screen");
                JDialogBLaucher jDialogBLaucher = JDialogBLaucher.getNewInstance(this.applicationProperties,
                        this.categoryService.getCategoryRepository());
                if (!jDialogBLaucher.isVisible()) {
                    jDialogBLaucher.setVisible(true);
                }
                jDialogBLaucher.toFront();
            }

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jnativehook.mouse.NativeMouseListener#nativeMouseReleased(org.
     * jnativehook.mouse.NativeMouseEvent)
     */
    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {

    }

}
