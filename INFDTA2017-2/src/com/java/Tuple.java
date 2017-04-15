import java.util.Vector;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class Tuple<T, E> extends Vector {

        public T t;
        public E e;
        public Tuple(T t, E e) {
            this.e = e;
            this.t = t;
        }
        public Tuple() {

        }

    public Tuple setE(E e) {
        this.e = e;
        return this;
    }

    public Tuple setT(T t) {
        this.t = t;
        return this;
    }

}
