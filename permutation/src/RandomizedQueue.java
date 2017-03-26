import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (n == items.length) {
            resize(2 * items.length);
        }

        items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        throwIfEmpty();
        swap(items, pickRandomId(), n - 1);
        Item item = items[--n];
        items[n] = null;
        if (n > 0 && n == items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        throwIfEmpty();
        return items[pickRandomId()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator(items, n);
    }

    private class RandomIterator implements Iterator<Item> {
        private Item[] randomItems;
        private int i;

        RandomIterator(Item[] items, int n) {
            randomItems = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                randomItems[i] = items[i];
                swap(randomItems, StdRandom.uniform(i + 1), i);
            }
        }

        @Override
        public boolean hasNext() {
            return i != randomItems.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return randomItems[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    private void swap(Item[] array, int from, int to) {
        Item old = array[from];
        array[from] = array[to];
        array[to] = old;
    }

    private int pickRandomId() {
        return StdRandom.uniform(size());
    }

    private void throwIfEmpty() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        for (String s : rq ) {
            System.out.println(s);
        }
        if (rq.size() != 3) {
            System.out.println("NOT EQUALS size1");
        }

        rq.dequeue();
        if (rq.size() != 2) {
            System.out.println("NOT EQUALS size2");
        }
    }
}