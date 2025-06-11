package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClearCacheService {

    private final CacheManager cacheManager;

    public void clearAllCaches() {
        for (String name : cacheManager.getCacheNames()) {
            clearCache(name);
        }
    }

    public void clearCache(String name) {
        if (cacheManager.getCache(name) != null) {
            cacheManager.getCache(name).clear();
        }
    }
}
