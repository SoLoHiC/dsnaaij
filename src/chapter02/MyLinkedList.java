package chapter02;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author chuck
 * @since 11/7/15
 */
public class MyLinkedList<T> implements Iterable<T> {

  private int theSize;
  private int modCount = 0;
  private Node<T> head;
  private Node<T> tail;

  public MyLinkedList() {
    clear();
  }

  public void clear() {
    head = new Node<>(null, null, null);
    tail = new Node<>(null, head, null);
    head.next = tail;

    theSize = 0;
    modCount++;
  }

  public int size() {
    return theSize;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean add(T x) {
    add(size(), x);
    return true;
  }

  public void add(int idx, T x) {
    addBefore(getNode(idx), x);
  }

  public T get(int idx) {
    return getNode(idx).data;
  }

  public T set(int idx, T newValue) {
    Node<T> p = getNode(idx);
    T oldValue = p.data;
    p.data = newValue;
    return oldValue;
  }

  public T remove(int idx) {
    return remove(getNode(idx));
  }

  private void addBefore(Node<T> p, T x) {
    Node<T> newNode = new Node<>(x, p.prev, p);
    newNode.prev.next = newNode;
    p.prev = newNode;
    theSize++;
    modCount++;
  }

  private T remove(Node<T> p) {
    p.next.prev = p.prev;
    p.prev.next = p.next;
    theSize--;
    modCount++;

    return p.data;
  }

  private Node<T> getNode(int idx) {
    Node<T> p;

    if (idx < 0 || idx > size()) {
      throw new IndexOutOfBoundsException();
    }

    if (idx < size() / 2) {
      p = head.next;
      for (int i = 0; i < idx; i++) {
        p = p.next;
      }
    } else {
      p = tail.prev;
      for (int i = size(); i > idx; i--) {
        p = p.prev;
      }
    }

    return p;
  }

  private static class Node<T> {
    public T data;
    public Node<T> prev;
    public Node<T> next;

    public Node(T d, Node<T> p, Node<T> n) {
      data = d;
      prev = p;
      next = n;
    }
  }

  private class LinkedListIterator implements Iterator<T> {

    // for iterator, it's inner cursor pointed at currently, for user, it is always the next to get
    private Node<T> current = head;
    // if the LinkedList only modified via its iterator, it's ok. if it's modified both via its
    // iterator and its built-in method, ConcurrentModificationException will be throw
    private int expectedModCount = modCount;
    //keep user remove the current node only once before calling another next() to get the next
    // one that removable
    private boolean okToRemove = false;

    @Override
    public boolean hasNext() {
      return current != tail;
    }

    @Override
    public T next() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      T nextItem = current.data;
      current = current.next;
      okToRemove = true;
      return nextItem;
    }

    @Override
    public void remove() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (!okToRemove) {
        throw new IllegalStateException();
      }

      MyLinkedList.this.remove(current.prev);
      okToRemove = false;
      expectedModCount++;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new LinkedListIterator();
  }
}
