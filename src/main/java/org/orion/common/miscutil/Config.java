package org.orion.common.miscutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static Properties config;

    private static void initConfiguration() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("orion.properties");
            properties.load(inputStream);
            config = properties;
        } catch (Exception e) {

        }

    }

    public static String get(String key) {
        if (config == null)
            initConfiguration();
        return config.getProperty(key);
    }

    public static void reload() {
        initConfiguration();
    }
}
