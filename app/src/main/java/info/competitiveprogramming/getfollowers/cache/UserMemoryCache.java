package info.competitiveprogramming.getfollowers.cache;

import android.support.v4.util.LruCache;

import info.competitiveprogramming.getfollowers.model.User;

public class UserMemoryCache {
    private LruCache<String, User> mMap;

    private UserMemoryCache() {
        //100kbを確保
        final int cacheSize = 1024 * 100;

        mMap = new LruCache<String, User>(cacheSize);
    }

    public User getUser(String key) {
        synchronized(mMap) {
            User user = mMap.get(key);
            if (user == null) {
                return null;
            } else {
                return user;
            }
        }
    }

    public void put(String key, User user) {
        if (user == null) return;
        synchronized (user) {
            User oldUser = mMap.put(key, user);
            if (oldUser != null) {
                oldUser = null;
            }
        }
    }

    public void clear() {
        synchronized (mMap) {
            mMap.evictAll();
        }
    }

    private static UserMemoryCache searchMemoryCache;

    public static UserMemoryCache getInstance() {
        if (searchMemoryCache == null) {
            searchMemoryCache = new UserMemoryCache();
        }
        return searchMemoryCache;
    }
}
