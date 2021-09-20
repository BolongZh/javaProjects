package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        this.comparator = c;
    }

    public T max() {
        if (this.size() == 0) {
            return null;
        }
        else {
            int pos = 0;
            T ret = this.get(pos);
            while (pos < this.size()) {
                if (comparator.compare(this.get(pos),ret)>0) {
                    ret = this.get(pos);
                }
                pos += 1;
            }
            return ret;
        }
    }

    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }
        else {
            int pos = 0;
            T ret = this.get(pos);
            while (pos < this.size()) {
                if (c.compare(this.get(pos),ret)>0) {
                    ret = this.get(pos);
                }
                pos += 1;
            }
            return ret;
        }
    }

}
