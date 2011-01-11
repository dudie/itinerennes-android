package fr.itinerennes.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Map} with a fixed length.
 * 
 * @author Jérémie Huchet
 * @param <K>
 *            the type used for keys
 * @param <V>
 *            the type used for values
 */
public class ArrayMap<K, V> implements Map<K, V> {

    /** The initial capacity. */
    private final transient int intialCapacity;

    /** The elements stored in the map. */
    private transient ArrayMapEntry<K, V>[] elements;

    /** The current number of elements in the map. */
    private transient int modCount = 0;

    /**
     * Creates a simple array map.
     * 
     * @param intialCapacity
     *            the initial capacity of the map
     */
    public ArrayMap(final int intialCapacity) {

        this.intialCapacity = intialCapacity;
        this.elements = newElementArray(intialCapacity);
    }

    /**
     * Creates an array of {@link ArrayMapEntry} of the given size.
     * 
     * @param size
     *            the size of the array to create.
     * @return a new array of {@link ArrayMapEntry} of the given size
     */
    @SuppressWarnings("unchecked")
    private ArrayMapEntry<K, V>[] newElementArray(final int size) {

        return new ArrayMapEntry[size];
    }

    /**
     * Simple map entry.
     * 
     * @author Jérémie Huchet
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     */
    private static final class ArrayMapEntry<K, V> implements Entry<K, V> {

        /** The entry key. */
        private K key;

        /** The entry value. */
        private V value;

        @Override
        public K getKey() {

            return key;
        }

        @Override
        public V getValue() {

            return value;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Map.Entry#setValue(java.lang.Object)
         */
        @Override
        public V setValue(final V value) {

            this.value = value;
            return value;
        }
    }

    /**
     * A {@link Set} with a fixed length.
     * 
     * @author Jérémie Huchet
     * @param <T>
     *            the type of the elements
     */
    private static final class ArraySet<T> implements Set<T> {

        /** The initial capacity of the set. */
        private transient int initialCapacity;

        /** The element values. */
        private transient T[] elements;

        /** The current number of elements in the set. */
        private transient int modCount = 0;

        /**
         * Creates a new simple array set.
         * 
         * @param initialCapacity
         *            the initial capacity of the set
         */
        public ArraySet(final int initialCapacity) {

            this.initialCapacity = initialCapacity;
            this.elements = newElementArray(initialCapacity);
        }

        /**
         * Creates an array of <code>T</code> of the given size.
         * 
         * @param size
         *            the size of the array to create.
         * @return a new array of <code>T</code> of the given size
         */
        @SuppressWarnings("unchecked")
        private T[] newElementArray(final int size) {

            return (T[]) new Object[size];
        }

        @Override
        public boolean add(final T value) {

            if (!contains(value)) {
                elements[0] = value;
                modCount++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean addAll(final Collection<? extends T> collection) {

            boolean changed = false;
            for (final T value : collection) {
                changed &= add(value);
            }
            return changed;
        }

        @Override
        public void clear() {

            this.elements = newElementArray(initialCapacity);
        }

        @Override
        public boolean contains(final Object value) {

            for (final T element : elements) {
                if (null == value && null == element || null != element && element.equals(value)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(final Collection<?> collection) {

            boolean containsAll = true;
            final Iterator<?> i = collection.iterator();
            while (i.hasNext() && containsAll) {
                containsAll &= contains(i.next());
            }
            return containsAll;
        }

        @Override
        public boolean isEmpty() {

            return 0 == modCount;
        }

        @Override
        public Iterator<T> iterator() {

            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean remove(final Object value) {

            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean removeAll(final Collection<?> collection) {

            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean retainAll(final Collection<?> collection) {

            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int size() {

            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object[] toArray() {

            return elements;
        }

        @Override
        public <R> R[] toArray(final R[] array) {

            return (R[]) elements;
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {

        elements = newElementArray(intialCapacity);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {

        for (final Entry<K, V> element : elements) {
            if (null == key && null == element.getKey() || key != null
                    && key.equals(element.getKey())) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(final Object value) {

        for (final Entry<K, V> element : elements) {
            if (null == value && null == element.getValue() || value != null
                    && value.equals(element.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V get(final Object key) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEmpty() {

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<K> keySet() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V put(final K key, final V value) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {

        // TODO Auto-generated method stub

    }

    @Override
    public V remove(final Object key) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {

        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Collection<V> values() {

        // TODO Auto-generated method stub
        return null;
    }
}
