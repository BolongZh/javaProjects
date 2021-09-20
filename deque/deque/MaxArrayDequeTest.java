package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

class MaxArrayDequeTest {

    public int compareTo(int a, int b) {
        return a - b;
    }

    public int reverseCompare(int a, int b) {
        return b - a;
    }

    public class sizeComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return compareTo((int) o1, (int) o2);
        }
    }

    public Comparator<Integer> getsizeComparator() {
        return new sizeComparator();
    }

    public class hotComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return reverseCompare((int) o1, (int) o2);
        }
    }

    public Comparator<Integer> gethotComparator() {
        return new hotComparator();
    }


    @Test
    void max() {
        Deque<Integer> ad = new MaxArrayDeque<Integer>(getsizeComparator());

        // assertNull(((MaxArrayDeque<Integer>) ad).max());
        ad.addFirst(1);
        ad.addLast(2);
        ad.addFirst(3);
        ad.addLast(99);
        ad.addLast(55);

        //assertEquals(99, (int) ad.max());
        //assertEquals(1, (int) ad.max(gethotComparator()));

    }

}