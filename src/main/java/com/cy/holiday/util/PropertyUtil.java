package com.cy.holiday.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyUtil {

    private static final String DEFAULT_PROPERTY_FILE_PATH = "config.properties";
    private static final Properties props = new Properties();

    private PropertyUtil() {
    }

    static {
        load();
    }

    /**
     * Get property from config file.
     *
     * @param key the key to search.
     * @return the value found.
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Load default config.properties.
     */
    private static void load() {
        InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTY_FILE_PATH);
        if (is == null) {
            return;
        }

        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);

        try {
            props.load(isr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
