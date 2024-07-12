package bg.duosoft.uniapplicationdemo.config.cache;

import bg.duosoft.uniapplicationdemo.services.base.BaseService;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.support.NoOpCacheManager;

import java.util.Collection;
import java.util.Collections;

public class CrudCacheResolver implements CacheResolver {

    private final CacheManager defaultCacheManager;

    private final NoOpCacheManager noOpCacheManager;

    public CrudCacheResolver(CacheManager defaultCacheManager, NoOpCacheManager noOpCacheManager) {
        this.defaultCacheManager = defaultCacheManager;
        this.noOpCacheManager = noOpCacheManager;
    }

    @Override
    public Collection<? extends org.springframework.cache.Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        if (context.getTarget() instanceof BaseService) {
            BaseService service = (BaseService) context.getTarget();
            Cache cache = getCache(service);

            return Collections.singletonList(cache);
        } else {
            throw new RuntimeException("CrudCacheResolver should be used for classes that extend BaseService.");
        }
    }

    private @NotNull Cache getCache(BaseService service) {
        CacheManager cacheManager = service.isCachingEnabled() ? defaultCacheManager : noOpCacheManager;
        String cacheName = service.getCacheName();
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null && service.isCachingEnabled()) {
            cacheManager.getCache(cacheName);
            cache = cacheManager.getCache(cacheName);
        }

        if (cache == null) {
            throw new RuntimeException("Failed to create cache: " + cacheName);
        }
        return cache;
    }
}
