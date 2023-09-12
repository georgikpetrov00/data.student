package com.grandp.data.hasher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class PasswordHashHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(PasswordHashHelper.class);
    public static int saltSize;

    public static int iterations;

    static {
        Properties properties = new Properties();

        try {
            Resource resource = new DefaultResourceLoader().getResource("classpath:config/password.properties");
            properties.load(resource.getInputStream());

            saltSize = Integer.parseInt(properties.getProperty("salt.size"));
            iterations = Integer.parseInt(properties.getProperty("iterations"));

            LOGGER.info(String.format("Successfully loaded password configuration with salt.size='%s' and iterations='%s'.", saltSize, iterations));
        } catch (IOException ex) {
            LOGGER.warn("Error while loading configuration for password hashing. Check password.properties. Will use default config: salt.size=32.", ex);
        }
    }
}
