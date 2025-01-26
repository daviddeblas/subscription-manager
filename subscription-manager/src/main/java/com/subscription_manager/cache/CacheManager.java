package com.subscription_manager.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheManager handles the caching of user subscription data.
 */
@Component
public class CacheManager {

    // In-memory cache for subscriptions
    // Key = userKey (e.g., user's unique ID)
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // Maximum validity of the cache (30 minutes)
    private static final long CACHE_DURATION_MS = 30 * 60 * 1000;

    /**
     * Retrieves the cached data for a user if it's still valid.
     *
     * @param userKey Unique identifier for the user
     * @return Cached list of subscriptions or null if not present/expired
     */
    public List<Map<String, Object>> getCachedSubscriptions(String userKey) {
        CacheEntry cacheEntry = cache.get(userKey);
        if (cacheEntry != null) {
            long ageMs = System.currentTimeMillis() - cacheEntry.getTimestamp();
            if (ageMs < CACHE_DURATION_MS) {
                return cacheEntry.getJsonList();
            } else {
                cache.remove(userKey);
            }
        }
        return null;
    }

    /**
     * Stores the subscription data in the cache for a user.
     *
     * @param userKey   Unique identifier for the user
     * @param jsonList  List of subscription data to cache
     */
    public void cacheSubscriptions(String userKey, List<Map<String, Object>> jsonList) {
        CacheEntry newEntry = new CacheEntry(jsonList);
        cache.put(userKey, newEntry);
    }

    /**
     * Inner class in order to represent a cache entry.
     */
    @Getter
    private static class CacheEntry {
        private final List<Map<String, Object>> jsonList;
        private final long timestamp;

        public CacheEntry(List<Map<String, Object>> jsonList) {
            this.jsonList = jsonList;
            this.timestamp = System.currentTimeMillis();
        }

    }
}

