package deque;

import javax.xml.stream.events.StartDocument;

public class ArrayDeque<T> implements Deque<T> {

    private int size;
    private int startIndex;
    private int endIndex;
    private T[] a = (T[]) new Object[8];

    public ArrayDeque() {
        this.size = 0;
        this.startIndex = 3;
        this.endIndex = 4;
    }

//    public ArrayDeque(int capacity) {
//        if (capacity<=8) {
//            throw new IllegalArgumentException("Please use the other constructor");
//        }
//        this.size = 0;
//        this.startIndex = 3;
//        this.startIndex = 4;
//        this.a = (T[]) new Object[capacity];
//    }


    @Override
    public void addFirst(T t) {
        if(size<a.length) {
            a[startIndex] = t;
            startIndex -= 1;
            size += 1;
            if (startIndex<0) {
                startIndex = a.length - 1;
            }
        }
        else {
            T[] res = (T[]) new Object[size*2];
            res[0] = t;
            if (startIndex == size-1) {
                System.arraycopy(a, 0, res, 1, size);
                startIndex = size*2-1;
                endIndex = size+1;
            }
            else {
                System.arraycopy(a,startIndex+1, res,1, size-startIndex-1);
                System.arraycopy(a, 0, res, size-startIndex, startIndex+1);
                startIndex = size*2-1;
                endIndex = size+1;
            }
            a = res;
            size += 1;
        }
    }

    @Override
    public void addLast(T t) {
        if(size<a.length) {
            a[endIndex] = t;
            endIndex += 1;
            size += 1;
            if (endIndex>=a.length) {
                endIndex = endIndex - a.length;
            }
        }
        else {
            T[] res = (T[]) new Object[size*2];
            res[size] = t;
            if (startIndex == size-1) {
                System.arraycopy(a, 0, res, 0, size);
            }
            else {
                System.arraycopy(a,startIndex+1, res,0, size-startIndex-1);
                System.arraycopy(a, 0, res, size-startIndex-1, startIndex+1);
            }
            startIndex = size*2-1;
            endIndex = size+1;
            a = res;
            size += 1;
        }
    };

    @Override
    public int size() {
        return size;
    };

    public void printDeque() {
        if (size == 0) {
            System.out.println("");
            return;
        }
        int pos = 0;
        while (pos<size-1) {
            System.out.print(this.get(pos) + " ");
            pos += 1;
        }
        System.out.print(this.get(pos));
        System.out.println("");

    };

    @Override
    public T removeFirst() {
        T ret;
        if (size == 0) {
            return null;
        }
        else {
            if (startIndex+1>= a.length) {
                startIndex = 0;
                ret = a[startIndex];
                a[startIndex] = null;
            }
            else {
                startIndex += 1;
                ret = a[startIndex];
                a[startIndex] = null;
            }
        }
        size -= 1;
        // resize when the occupying factor is less than or equal to 25%
        // temporarily cast integer as double to support true division
        if (((1.0*(size))/(a.length*1.0)<=0.25)&&(a.length>8)) {
            T[] res = (T[]) new Object[size];
            if (startIndex == a.length - 1) {
                System.arraycopy(a, 0, res,0, res.length);
                startIndex = res.length - 1;
                endIndex = 0;

            }
            else {
                if (startIndex <= endIndex) {
                    System.arraycopy(a, startIndex + 1, res, 0, size);
                    startIndex = res.length - 1;
                    endIndex = 0;
                }
                else {
                    System.arraycopy(a,startIndex+1,res,0, a.length -startIndex - 1);
                    System.arraycopy(a, 0, res, a.length-startIndex-1, size - a.length + startIndex + 1);
                    startIndex = res.length - 1;
                    endIndex = 0;
                }
            }
        a = res;
        }
        return ret;
    };

    @Override
    public T removeLast() {
        T ret;
        if (size == 0) {
            return null;
        }
        else {
            if (endIndex == 0) {
                endIndex = a.length - 1;
                ret = a[endIndex];
                a[endIndex] = null;
            }
            else {
                endIndex -= 1;
                ret = a[endIndex];
                a[endIndex] = null;
            }
        }
        size -= 1;
        // resize when the occupying factor is less than or equal to 25%
        // temporarily cast integer as double to support true division
        if (((1.0*(size))/(a.length*1.0)<=0.25)&&(a.length>8)) {
            T[] res = (T[]) new Object[size];
            if (startIndex == a.length - 1) {
                System.arraycopy(a, 0, res,0, res.length);
                startIndex = res.length - 1;
                endIndex = 0;

            }
            else {
                if (startIndex <= endIndex) {
                    System.arraycopy(a, startIndex + 1, res, 0, size);
                    startIndex = res.length - 1;
                    endIndex = 0;
                }
                else {
                    System.arraycopy(a,startIndex+1,res,0, a.length -startIndex - 1);
                    System.arraycopy(a, 0, res, a.length-startIndex-1, size - a.length + startIndex + 1);
                    startIndex = res.length - 1;
                    endIndex = 0;
                }
            }
            a = res;
        }
        return ret;
    };

    @Override
    public T get(int index) {
        if (index>=size) {
            return null;
        }
        int truePos = startIndex + index + 1;
        if (truePos>= a.length) {
            truePos -= a.length;
        }

        return a[truePos];
    };





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