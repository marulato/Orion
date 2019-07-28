package org.orion.common.init;

import org.orion.common.miscutil.Config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.lang.reflect.Method;

public class ConfigInitializer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Class<Config> configClass = Config.class;
        Method init = configClass.getDeclaredMethod("init");
        init.setAccessible(true);
        init.invoke(null);
    }
}
