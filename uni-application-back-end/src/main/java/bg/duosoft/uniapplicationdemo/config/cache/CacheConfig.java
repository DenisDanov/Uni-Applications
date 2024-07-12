package bg.duosoft.uniapplicationdemo.config.cache;

import lombok.RequiredArgsConstructor;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.Caching;
import java.net.URISyntaxException;
import java.util.Objects;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final NoOpCacheManagerBean noOpCacheManager;

    @Bean
    @Primary
    public CacheManager defaultCacheManager() throws URISyntaxException {
        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider();
        javax.cache.CacheManager jCacheManager = provider.getCacheManager(
                Objects.requireNonNull(getClass().getResource("/config/ehcache.xml")).toURI(), getClass().getClassLoader());
        return new JCacheCacheManager(jCacheManager);
    }

    @Bean
    public CacheResolver crudCacheResolver() throws URISyntaxException {
        return new CrudCacheResolver(defaultCacheManager(), noOpCacheManager.noOpCacheManager());
    }
}
