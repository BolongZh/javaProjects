package deque;

import edu.princeton.cs.algs4.In;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */
public class LinkedListDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> lld = new LinkedListDeque<Integer>();
    public static Deque<String>  sad = new LinkedListDeque<String>();
    public static Deque<Integer> llld = new LinkedListDeque<Integer>();
    public static Deque<Integer> ad = new ArrayDeque<Integer>();


    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

		assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
		lld.addFirst(0);

        assertFalse("lld should now contain 1 item", lld.isEmpty());

        // Reset the linked list deque at the END of the test.
        lld = new LinkedListDeque<Integer>();

    }

    @Test
    public void addGetFirstTest() {

        lld.addFirst(0);
        assertTrue(lld.get(0)==0);
        lld.addFirst(2);
        assertTrue(lld.get(1)==0);

        lld = new LinkedListDeque<Integer>();

    }

    @Test
    public void addGetLastFirstTest() {
        lld.addLast(0);
        assertTrue(lld.get(0)==0);
        lld.addFirst(2);
        lld.addLast(3);
        assertTrue(lld.get(0)==2);
        assertTrue(lld.get(2)==3);

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void sizeTest() {

        // empty array should have length 0
        assertEquals(0,lld.size());
        lld.addLast(1);
        lld.addLast(2);
        lld.addFirst(3);
        assertEquals(3,lld.size());

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeTest() {

        // removing first and last on empty array should return null
        assertNull(lld.removeFirst());
        assertNull(lld.removeLast());

        lld.addFirst(3);
        lld.addLast(2);
        lld.addFirst(1);

        assertTrue(lld.removeLast()==2);
        assertTrue(lld.removeFirst()==1);
        assertTrue(lld.removeFirst()==3);
        assertTrue(lld.removeFirst()==null);

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void getTest() {
        assertNull(lld.get(0));

        lld.addFirst(3);
        lld.addFirst(2);
        lld.addFirst(1);

        assertTrue(1 == lld.get(0));
        assertTrue(2 == lld.get(1));
        assertTrue(3 == lld.get(2));

        //assertTrue(lld.get(2) == lld.getRecursive(2));

        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void StringTest() {

        sad.addFirst("j");
        sad.addLast("i");
        sad.addFirst("er");

        assertEquals("er",sad.get(0));
        assertEquals("i",sad.removeLast());

        sad = new LinkedListDeque<String>();
    }

    @Test
    public void equalsTest() {
        llld.addFirst(1);
        lld.addFirst(1);
        assertEquals(lld,llld);
        llld.removeFirst();
        lld.removeLast();
        assertEquals(lld,llld);
        lld.addFirst(1);
        assertNotEquals(lld,llld);

        lld = new LinkedListDeque<Integer>();
        llld = new LinkedListDeque<Integer>();

        lld.addFirst(1);
        ad.addFirst(1);
        assertEquals(lld,ad);
        ad.addLast(2);
        lld.addLast(3);
        assertNotEquals(ad,lld);

        lld = new LinkedListDeque<Integer>();
        llld = new LinkedListDeque<Integer>();
        ad = new ArrayDeque<Integer>();

    }
}


