import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int n;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkAddItem(item);

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (first.next != null) {
            first.next.previous = first;
        }
        if (last == null) {
            last = first;
        }
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        checkAddItem(item);
        Node node = last;
        last = new Node();
        last.item = item;
        last.previous = node;
        if (last.previous != null) {
            last.previous.next = last;
        }
        if (first == null) {
            first = last;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkRemoveItem();
        Node node = first;
        first = first.next;
        if (first != null) {
            first.previous = null;
        } else {
            last = null;
        }
        n--;
        return node.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkRemoveItem();
        Node node = last;
        last = last.previous;
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }
        n--;
        return node.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new AscendingIterator(first);
    }

    private class AscendingIterator implements Iterator<Item> {
        private Node first;

        AscendingIterator(Node first) {
            this.first = first;
        }

        @Override
        public boolean hasNext() {
            return first != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = first;
            first = first.next;
            return node.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void checkAddItem(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    private void checkRemoveItem() {
        if (n == 0) {
            throw new NoSuchElementException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> d = new Deque<>();

        d.addFirst("a");
        d.addFirst("b");
        for (String s : d) {
            System.out.println(s);
        }
        String item = d.removeLast();
        if (!item.equals("a")) {
            System.out.println("NOT EQUALS remove last");
        }
        item = d.removeFirst();
        if (!item.equals("b")) {
            System.out.println("NOT EQUALS remove first");
        }
        if (d.size() != 0) {
            System.out.println("NOT EQUALS size");
        }
        d.addLast("c");
        int a = 0;
    }
}
