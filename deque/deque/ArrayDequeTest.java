package deque;

import org.junit.Test;

import static org.junit.Assert.*;

/* Performs some basic array deque tests. */
public class ArrayDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

     public static Deque<Integer> ad = new ArrayDeque<Integer>();
     public static Deque<String> sad = new ArrayDeque<String>();
     public static Deque<Integer> aad = new ArrayDeque<Integer>();
     public static Deque<Integer> lld = new LinkedListDeque<Integer>();
    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        assertTrue("A newly initialized adeque should be empty", ad.isEmpty());
        ad.addFirst(0);

        assertFalse("ad should now contain 1 item", ad.isEmpty());

        // Reset the linked list deque at the END of the test.
        ad = new ArrayDeque<Integer>();

    }

    @Test
    public void addGetFirstTest() {

        ad.addFirst(0);
        assertTrue(ad.get(0)==0);
        ad.addFirst(2);
        assertTrue(ad.get(1)==0);
        assertTrue(ad.get(0)==2);

        ad = new ArrayDeque<Integer>();

    }

    @Test
    public void addGetLastFirstTest() {
        ad.addLast(0);
        assertTrue(ad.get(0)==0);
        ad.addFirst(2);
        ad.addLast(4);
        assertTrue(ad.get(0)==2);
        assertTrue(ad.get(2)==4);
        ad.addFirst(2);
        ad.addFirst(2);
        ad.addFirst(2);
        ad.addFirst(2);
        ad.addFirst(2);
        ad.addFirst(3);
        assertEquals(9,ad.size());
        assertEquals(3, (int) ad.get(0));
        assertEquals(4,(int) ad.get(8));
        assertEquals(0,(int) ad.get(7));
        ad.addLast(99);
        assertEquals(99, (int) ad.get(9));
        assertEquals(4,(int) ad.get(8));
        assertEquals(10, ad.size());


        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void sizeTest() {

        // empty array should have length 0
        assertEquals(0,ad.size());
        ad.addLast(1);
        ad.addLast(2);
        ad.addFirst(3);
        assertEquals(3,ad.size());

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeTest() {

        // removing first and last on empty array should return null
        assertNull(ad.removeFirst());
        assertNull(ad.removeLast());

        ad.addFirst(3);
        ad.addLast(2);
        ad.addFirst(1);

        assertTrue(ad.removeLast()==2);
        assertTrue(ad.removeFirst()==1);
        assertTrue(ad.removeFirst()==3);
        assertTrue(ad.removeFirst()==null);

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void crazyRemoveTest() {
        ad.addFirst(3); //removed
        ad.addFirst(4);
        ad.addFirst(5);
        ad.addFirst(6);
        ad.addFirst(3);  //removed
        ad.addFirst(4);  //removed
        ad.addFirst(5);  //removed
        ad.addFirst(6);  //removed
        ad.addFirst(3);  //removed
        ad.addFirst(4);  //removed
        ad.addLast(5);  //removed
        ad.addFirst(6);  //removed

        // sanity checking size
        assertEquals(12,ad.size());
        assertEquals(5, (int) ad.removeLast());
        assertEquals(6, (int) ad.removeFirst());
        assertEquals(4, (int) ad.removeFirst());
        assertEquals(3, (int) ad.removeLast());
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        assertEquals(3,ad.size()); // check resizing
        assertEquals(6, (int) ad.get(0));
        assertEquals(5, (int) ad.get(1));
        assertEquals(4, (int) ad.get(2));
        assertNull(ad.get(3));

        ad = new ArrayDeque<Integer>();
        ad.addFirst(3); //removed
        ad.addFirst(4);
        ad.addFirst(5);
        ad.addFirst(6);
        ad.addFirst(3);  //removed
        ad.addFirst(4);  //removed
        ad.addFirst(5);  //removed
        ad.addFirst(6);  //removed
        ad.addFirst(3);  //removed
        ad.addFirst(4);  //removed
        ad.addLast(5);  //removed
        ad.addFirst(6);  //removed
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        assertEquals(2,ad.size()); // check resizing
        assertEquals(6, (int) ad.get(0));
        assertEquals(4, (int) ad.get(1));
        assertNull(ad.get(2));

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest() {
        assertNull(ad.get(0));

        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(1);

        assertTrue(1 == ad.get(0));
        assertTrue(2 == ad.get(1));
        assertTrue(3 == ad.get(2));

        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void StringTest() {

        sad.addFirst("j");
        sad.addLast("i");
        sad.addFirst("er");

        assertEquals("er",sad.get(0));
        assertEquals("i",sad.removeLast());
    }

    @Test
    public void equalsTest() {
        aad.addFirst(1);
        ad.addFirst(1);
        assertEquals(ad,aad);
        aad.removeFirst();
        ad.removeLast();
        assertEquals(ad,aad);
        ad.addFirst(2);
        assertNotEquals(ad,aad);

        ad = new ArrayDeque<Integer>();
        aad = new ArrayDeque<Integer>();

        lld = new LinkedListDeque<Integer>();


        lld.addFirst(1);
        ad.addFirst(1);
        assertEquals(lld,ad);
        ad.addLast(2);
        lld.addLast(3);
        assertNotEquals(ad,lld);

        ad = new ArrayDeque<Integer>();
        aad = new ArrayDeque<Integer>();

        lld = new LinkedListDeque<Integer>();
    }
}

