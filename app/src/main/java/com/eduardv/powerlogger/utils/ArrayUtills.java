package com.eduardv.powerlogger.utils;

import java.util.List;
import java.util.function.Predicate;

public class ArrayUtills {
    /*
     * Iterates over a list to find a given item using shallow equality
     * */
    public static <T> int findIndexInArray(List<T> array, T item) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == item) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int findIndexByPredicate(List<T> list, Predicate<T> p) {
        int index = 0;
        for (T o : list) {
            if (p.test(o)) {
                break;
            }
            index++;
        }

        if (index == list.size()) {
            return -1;
        }

        return index;
    }
}
