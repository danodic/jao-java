package com.danodic.jao.pools;

import java.util.HashMap;
import java.util.Map;

/**
 * The shared resource pool can be used by implementations of IActions and
 * IRenderers to avoid initializing the same resource twice in case of cloned
 * instances. That can be used by fonts or images that are shared across the
 * different instances of the same renderer. This is a singleton, so it must be
 * acessed using SharedResourcePool.getPool().
 *
 * @author danodic
 */
public class SharedResourcePool {

    private static SharedResourcePool pool;
    private final Map<String, Object> data;

    private SharedResourcePool() {
        data = new HashMap<>();
    }

    /**
     * Returns the main instance of SharedResourcePool.
     *
     * @return
     */
    public static SharedResourcePool getPool() {
        if (pool == null) {
            pool = new SharedResourcePool();
        }
        return pool;
    }

    /**
     * Get data from the shared resource pool.
     *
     * @param <T> Type of data expected to be returned.
     * @param name Name of data to be retrieved.
     * @return Whatever you have stored at the key specified.
     */
    public <T> T getData(String name) {
        return (T) data.get(name);
    }

    /**
     * Checks if a given key exists in the list of data.
     *
     * @param name Name of the data to be checked.
     * @return True or False.
     */
    public boolean hasData(String name) {
        return data.containsKey(name);
    }

    /**
     * Adds an element to the shared resource pool.
     *
     * @param key 
     * @param value
     */
    public void addData(String key, Object value) {
        data.put(key, value);
    }

}
