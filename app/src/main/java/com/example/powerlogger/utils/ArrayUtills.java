package com.example.powerlogger.utils;

import java.util.List;

public class ArrayUtills {
    /*
    * Iterates over a list to find a given item using shallow equality
    * */
    public static <T> int findIndexInArray (List<T> array, T item) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == item) {
                return i;
            }
        }
        return  -1;
    }
}
