package fr.itinerennes.business;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages a cache in memory.
 * 
 * @author Jérémie Huchet
 */
public class RemoteDataCacheProvider {

    /** The map containing the cached values. */
    private static final ConcurrentHashMap<CacheKey, CacheEntry> cache = new ConcurrentHashMap<CacheKey, CacheEntry>();

    /**
     * Puts a new value in the cache.
     * 
     * @param id
     *            the identifier of the value
     * @param value
     *            the value to cache
     */
    public static void put(final Object id, final Object value) {

        cache.put(new CacheKey(id, value.getClass()), new CacheEntry(value));
    }

    /**
     * Gets a previously cached value.
     * 
     * @param <T>
     *            the type of the value
     * @param clazz
     *            the class of the value
     * @param id
     *            the key of the value
     * @return the requested value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(final Class<T> clazz, final Object id) {

        final CacheEntry entry = cache.get(new CacheKey(id, clazz));
        if (null != entry) {
            return (T) entry.getValue();
        } else {
            return null;
        }
    }

    /**
     * Checks the cache contains a key.
     * 
     * @param clazz
     *            the class of the value
     * @param id
     *            the identifier of the value
     * @return true if this key exists in the cache
     */
    public static boolean contains(final Class<?> clazz, final Object id) {

        return cache.containsKey(new CacheKey(id, clazz));
    }

    /**
     * Checks the cache contains a value.
     * 
     * @param value
     *            the value
     * @return true if this value exists in the cache
     */
    public static boolean contains(final Object value) {

        return cache.contains(new CacheEntry(value));
    }

    /**
     * Represents a key for a cached value.
     * 
     * @author Jérémie Huchet
     */
    private static class CacheKey {

        /** The identifier. */
        private final Object id;

        /** The class name of the value this key identifies. */
        private final String className;

        /**
         * Creates a key.
         * 
         * @param id
         *            the identifier
         * @param clazz
         *            the class name of the value this key identifies
         */
        public CacheKey(final Object id, final Class<?> clazz) {

            this.id = id;
            this.className = clazz.getName();
        }

        /*
         * (non-javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            final int prime = 31;
            int result = 1;
            result = prime * result + ((className == null) ? 0 : className.hashCode());
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        /*
         * (non-javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {

            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final CacheKey other = (CacheKey) obj;
            if (className == null) {
                if (other.className != null)
                    return false;
            } else if (!className.equals(other.className))
                return false;
            if (id == null) {
                if (other.id != null)
                    return false;
            } else if (!id.equals(other.id))
                return false;
            return true;
        }
    }

    /**
     * Contains cache entry metadata.
     * 
     * @author Jérémie Huchet
     */
    private static class CacheEntry {

        /** The cached value. */
        private final Object value;

        /**
         * Creates a new cache entry.
         * 
         * @param value
         *            the cached value
         */
        public CacheEntry(final Object value) {

            this.value = value;
        }

        /**
         * Gets the value.
         * 
         * @return the value
         */
        public Object getValue() {

            return value;
        }
    }
}
