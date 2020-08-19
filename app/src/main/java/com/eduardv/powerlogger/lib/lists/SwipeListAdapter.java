package com.eduardv.powerlogger.lib.lists;

public interface SwipeListAdapter<T> {
    int AD_TYPE = 1;
    int VIEW_TYPE = 2;

    void removeItem(int position);

    void restoreItem(T item, int position);
}
