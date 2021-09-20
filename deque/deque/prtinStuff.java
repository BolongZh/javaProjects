package deque;

public class prtinStuff {
    public static void main(String[] args) {
        String dude = "332";
        dude.length();
        int ret = args.length;


        Deque<Integer> ad = new ArrayDeque<Integer>();
        System.out.println(Deque.class.isAssignableFrom(ad.getClass()));
        ad.addFirst(2);
        ad.addFirst(3);
        ad.addLast(4);
        ad.addFirst(2);
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(3);
        ad.addLast(4);
        ad.printDeque();

        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();
        ad.removeFirst();
        ad.printDeque();


        Deque<Integer> lld = new LinkedListDeque<Integer>();
        lld.addFirst(1);
        lld.addLast(2);
        lld.addFirst(3);
        lld.addFirst(1);
        lld.addLast(2);
        lld.addFirst(3);
        lld.addFirst(1);
        lld.addLast(2);
        lld.addFirst(3);
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();
        lld.removeLast();
        lld.printDeque();

    }
}
