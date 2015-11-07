package chapter02;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author chuck
 * @since 11/5/15
 */
public class MyArrayList<T> implements Iterable<T> {

  private static final int DEFAULT_CAPACITY = 10;

  private int theSize;
  private T[] theItems;

  public MyArrayList() {
    clear();
  }

  public void clear() {
    theSize = 0;
    ensureCapacity(DEFAULT_CAPACITY);
  }

  public int size() {
    return theSize;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public void trimToSize() {
    ensureCapacity(size());
  }

  public T get(int idx) {
    if (idx < 0 || idx >= size()) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return theItems[idx];
  }

  public T set(int idx, T newValue) {
    if (idx < 0 || idx >=size()) {
      throw new ArrayIndexOutOfBoundsException();
    }
    T old = theItems[idx];
    theItems[idx] = newValue;
    return old;
  }

  @SuppressWarnings({"unchecked"})
  public void ensureCapacity(int newCapacity) {
    if (newCapacity < theSize) {
      return;
    }
    T[] old = theItems;
    theItems = (T[]) new Object[newCapacity];
    System.arraycopy(old, 0, theItems, 0, size());
  }

  public boolean add(T x) {
    add(size(), x);
    return true;
  }

  public void add(int idx, T x) {
    if (theItems.length == size()) {
      ensureCapacity(size() * 2 + 1);
    }
    System.arraycopy(theItems, idx, theItems, idx + 1, theSize - idx);
    theSize++;
  }

  public T remove(int idx) {
    T removedItem = theItems[idx];
    System.arraycopy(theItems, idx + 1, theItems, idx, theSize - 1 - idx);
    theSize--;
    return removedItem;
  }

  @Override
  public Iterator<T> iterator() {
    return new ArrayListIterator();
  }

  private class ArrayListIterator implements Iterator<T> {

    private int current = 0;

    @Override
    public boolean hasNext() {
      return current < size();
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return theItems[current++];
    }

    @Override
    public void remove() {
      MyArrayList.this.remove(--current);
    }
  }
}
