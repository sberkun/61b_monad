import java.util.ArrayList;
import java.util.Arrays;

/* A function. Analogous to lambda functions in Python. This interface is also
*  provided in java.utils.function, along with other functional interface types  */
interface Function<T,R> {
    public R apply(T t);
}

/* You may be familiar with the concept of map() on objects, which takes in
*  a function and applies that function to all elements. bind() is a generalized
*  version of map(), where each element can become multiple elements, rather
*  than just one. For a more complete explanation of monads, check out
*  https://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
*  Here, M represents the "self" type */
interface Monad<M extends Monad<M, T>, T> {
    public<F extends Function<T, M>> M bind(F f);
}

class CheeseList<T> implements Monad<CheeseList<T>, T> {
    public ArrayList<T> ar;
    public CheeseList(ArrayList<T> ar) {
        this.ar = ar;
    }

    /* Should apply F to each element in ar, and returns
    *  the concatenated results. Does not modify ar. */
    public <F extends Function<T, CheeseList<T>>> CheeseList<T> bind(F f) {
        ArrayList<T> result = new ArrayList<>();
        for (T a : ar) {
            result.addAll(f.apply(a).ar);
        }
        return new CheeseList<T>(result);
    }
}

public class Main{
    public static void main(String[] args) {
        /* repeatN is a function that returns a list of N repeated N times. */
        Function<Integer, CheeseList<Integer>> repeatN = N -> {
            ArrayList<Integer> ar = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                ar.add(N);
            }
            return new CheeseList<>(ar);
        };
        CheeseList<Integer> cheese = new CheeseList<>(new ArrayList<>(Arrays.asList(3,1,4)));
        ArrayList<Integer> result = cheese.bind(repeatN).ar;
        // expected result: [3,3,3,1,4,4,4,4]
    }
}

