package deque;

public class LinkedListDeque<T> implements Deque<T> {

    // static or non-static?
    private class TNode {
        public T item;
        public TNode next;
        public TNode prev;

        public TNode() {

        }

        public TNode(TNode prev, T item, TNode next) {
            this.item = item;
            this.next = next;
            this.prev =prev;
        }
    }

    private TNode sentinel;
    private int size;

    public LinkedListDeque() {
        this.sentinel = new TNode();
        this.sentinel.prev = sentinel;
        this.sentinel.next = this.sentinel.prev;
        this.size = 0;
    }

    @Override
    public void addFirst(T t) {
        size += 1;
        TNode added = new TNode(sentinel,t,sentinel.next);
        sentinel.next.prev = added;
        sentinel.next = added;
    }

    @Override
    public void addLast(T t) {
        size += 1;
        TNode added = new TNode(sentinel.prev,t,sentinel);
        sentinel.prev.next = added;
        sentinel.prev = added;
    };

    @Override
    public int size() {
        return this.size;
    };

    public void printDeque() {
        if (sentinel.next==null) {
            System.out.println("");
        }
        TNode p = sentinel.next;
        while (p.next!=sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.print(p.item);
        System.out.println("");
    };

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T res = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return res;
    };

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T res = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return res;
    };

    @Override
    public T get(int index) {
        if (index >= size || index<0) {
            return null;
        }
        TNode p = sentinel.next;
        while (index>0) {
            p = p.next;
            index -= 1;
        }
        return p.item;

    };


    // recursively access an item
    private T recursiveHelper (TNode t, int index) {
        if (index == 0) {
            return t.next.item;
        }
        return recursiveHelper(t.next,index-1);
    }

    public T getRecursive(int index) {
        if (index >= size || index<0) {
            return null;
        }
        return recursiveHelper(sentinel, index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!Deque.class.isAssignableFrom(o.getClass())) {
            return false;
        }
        if (size != ((Deque<?>) o).size()) return false;


        int pos = 0;

        while (pos<size) {
            if (!this.get(pos).equals(((Deque<?>) o).get(pos))) return false;
            pos += 1;
        }
        return true;
    };
}