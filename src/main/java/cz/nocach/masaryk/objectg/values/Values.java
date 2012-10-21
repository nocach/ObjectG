package cz.nocach.masaryk.objectg.values;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *     Utility class for creating value with fluent api
 * </p>
 * <p>
 * User: __nocach
 * Date: 21.10.12
 * </p>
 */
public class Values {
    public static <T> ListBuilder<T> list(T... values) {
        return new ListBuilder(values);
    }

    public static class ListBuilder<T>{

        private T[] values;

        public ListBuilder(T... values) {
            this.values = values;
        }

        public ListBuilder<T> without(T... toRemove){
            List<T> original = Lists.newArrayList(values);
            original.removeAll(Arrays.asList(toRemove));
            values = original.toArray(toRemove);
            return this;
        }

        public T[] values(){
            return values;
        }
    }
}
