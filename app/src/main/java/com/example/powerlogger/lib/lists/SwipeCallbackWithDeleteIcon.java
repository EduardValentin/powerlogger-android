package com.example.powerlogger.lib.lists;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.powerlogger.R;

import java.util.function.Consumer;

public class SwipeCallbackWithDeleteIcon extends ItemTouchHelper.SimpleCallback {

    private final Drawable icon;
    private final ColorDrawable background;
    private RecyclerView.Adapter adapter;
    private Consumer<Integer> removeAction;

    public SwipeCallbackWithDeleteIcon(int dragDirs, int swipeDirs, RecyclerView.Adapter adapter, Consumer<Integer> removeAction, Context context) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
        this.removeAction = removeAction;

        icon = ContextCompat.getDrawable(context,
                R.drawable.ic_delete_sweep_black_24dp);

        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        View itemView = viewHolder.itemView;

        final int SWIPE_THRESHOLD = itemView.getWidth() / 3;

        if (position < SWIPE_THRESHOLD * 2) {
            removeAction.accept(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;

//        final int SWIPE_THRESHOLD = itemView.getWidth() / 3;

        int backgroundCornerOffset = 20;
        int alpha = Math.min(255, (int) Math.abs(dX) / 2);
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 4;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        int itemMiddleHeight = itemView.getHeight() / 2;

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left

            int iconOffset = icon.getIntrinsicWidth() - Math.min(icon.getIntrinsicWidth(), (int) Math.abs(dX) / 4);

            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth() + iconOffset;
            int iconRight = itemView.getRight() - iconMargin + iconOffset;

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.setAlpha(alpha);

//            int bgTop = Math.min(itemMiddleHeight, itemView.getTop() + (int) Math.abs(dX) / 2);
//            int bgBot = Math.max(itemMiddleHeight, itemView.getBottom() - (int) Math.abs(dX) / 2);

            background.setAlpha(alpha);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());

        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
