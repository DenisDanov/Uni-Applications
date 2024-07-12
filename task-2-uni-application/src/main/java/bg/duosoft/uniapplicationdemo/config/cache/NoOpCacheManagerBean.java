package bg.duosoft.uniapplicationdemo.config.cache;

import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NoOpCacheManagerBean {
    @Bean(name = "noOpCacheManager")
    public NoOpCacheManager noOpCacheManager() {
        return new NoOpCacheManager();
    }
}
