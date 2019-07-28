package org.orion.common.miscutil;

import org.orion.systemAdmin.entity.SystemConfig;
import org.orion.systemAdmin.service.SysConfigService;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static Properties config;
    private static Map<String, String> sysConfig;

    private static void init() throws Exception {
        initProperties();
        initSysConfig();
    }

    private static void initProperties() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("orion.properties");
        properties.load(inputStream);
        config = properties;
    }

    private static void initSysConfig() {
        SysConfigService sysConfigService = SpringUtil.getBean(SysConfigService.class);
        List<SystemConfig> sysConfigList = sysConfigService.getAllSystemConfigs();
        sysConfig = new HashMap<>();
        for (SystemConfig config : sysConfigList) {
            sysConfig.put(config.getConfigKey(), config.getConfigValue());
        }
    }

    public static String get(String key) {
        String value = null;
        try {
            initProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (config != null) {
            value = config.getProperty(key);
            if (value == null) {
                value = sysConfig.get(key);
            }
        }
        return value;
    }

}
