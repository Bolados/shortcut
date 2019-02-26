package dev.bscako.blaucher.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author BSCAKO
 *
 */

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties implements Serializable {

    private static final long serialVersionUID = 8712678076742370958L;

    private String name;

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


}
