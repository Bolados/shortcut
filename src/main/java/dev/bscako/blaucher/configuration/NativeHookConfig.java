
package dev.bscako.blaucher.configuration;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.mouse.NativeMouseListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author BSCAKO
 *
 */

@Component
public class NativeHookConfig implements Serializable {

    private static final long serialVersionUID = 7325426028723275292L;

    public NativeHookConfig() {
        // Get the logger for "org.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void startNativeHook(NativeMouseListener nativeMouseListener) {
        // Set the event dispatcher to a swing safe executor service.
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
        GlobalScreen.addNativeMouseListener(nativeMouseListener);

    }


    public void cleanNativeHook() {
        // Clean up the native hook.
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e1) {
            e1.printStackTrace();
        }
    }

}
