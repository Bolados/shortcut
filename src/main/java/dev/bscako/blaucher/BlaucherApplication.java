package dev.bscako.blaucher;

import dev.bscako.blaucher.configuration.ApplicationProperties;
import dev.bscako.blaucher.graphics.TrayMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.awt.*;
import java.io.Serializable;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class BlaucherApplication implements Serializable {


    private static final long serialVersionUID = -7242524483963407997L;
    private final Logger logger = LoggerFactory.getLogger(BlaucherApplication.class);


    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private TrayMenu trayMenu;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BlaucherApplication bLaucherApplication = (new SpringApplicationBuilder(BlaucherApplication.class)
                        .headless(false).run(args)).getBean(BlaucherApplication.class);

                bLaucherApplication.getTrayMenu().getSystemTray()
                        .setStatus("              ");

                bLaucherApplication.logger.info("Application started");


            } catch (Exception e) {
                LoggerFactory.getLogger(BlaucherApplication.class).debug("Error while starting application", e);
            }
        });
    }

    /**
     * @return the trayMenu
     */
    public TrayMenu getTrayMenu() {
        return this.trayMenu;
    }

    /**
     * @return the applicationProperties
     */
    public ApplicationProperties getApplicationProperties() {
        return this.applicationProperties;
    }

}
