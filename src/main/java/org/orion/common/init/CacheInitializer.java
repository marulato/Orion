package org.orion.common.init;

import org.orion.common.cache.CachePool;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheInitializer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        CachePool.init();
    }
}
