package advent.y2017.datastructures;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import advent.Util;

/**
 * Fixed Capacity Ring Buffer. Initialized with nulls.
 */
public class RingBuffer<E> extends AbstractList<E> implements RandomAccess {

    private final List<E> buf;  // a List implementing RandomAccess
    int bufSize;

    /**
     * Construct a buffer. All gets will return null until you initialize it with stuff. Buffer is accessed
     * with an index which starts at zero and increases to {@link Integer#MAX_VALUE}. Negative indices are
     * supported.
     *
     * @param capacity the buffer capacity
     */
    public RingBuffer(int capacity) {
        bufSize = capacity;
        buf = new ArrayList<>(Collections.nCopies(capacity, (E) null));
    }

    private int wrapIndex(int i) {
        int m = i % bufSize;
        if (m < 0) { // java modulus can be negative
            m += bufSize;
        }
        return m;
    }

    @Override
    public int size() {
        return bufSize;
    }

    @Override
    public E get(int i) {
       return buf.get(wrapIndex(i));
    }

    @Override
    public E set(int i, E e) {
        return buf.set(wrapIndex(i), e);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("The buffer is at capacity; the concept of \"end\" does not exist."
                + " Use add(i, e) instead.");
    }

    @Override
    public void add(int i, E e) {
        buf.add(wrapIndex(i), e);
    }

    @Override
    public E remove(int i) {
        int m = wrapIndex(i);
        return buf.remove(m);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        int size = 5;
        RingBuffer<Integer> testMe = new RingBuffer<>(size);

        testMe.add(2,2);
        Util.log("%s", testMe);

        for (int i=0; i<size; ++i) {
            testMe.add(i,i);
            Util.log("%s", testMe);
        }
        for (int i=-2*size; i<2*size; ++i) {
            Util.log("testMe.get(%d) = %d", i, testMe.get(i));
        }
    }
}