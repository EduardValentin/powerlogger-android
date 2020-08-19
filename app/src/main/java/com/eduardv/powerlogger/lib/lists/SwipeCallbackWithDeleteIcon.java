package com.eduardv.powerlogger.lib.lists;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.eduardv.powerlogger.ui.logger.LogListItemViewHolder;

public class SwipeCallbackWithDeleteIcon<VH extends SwipeListViewHolder> extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public SwipeCallbackWithDeleteIcon(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

//    @Override
//    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//        return false;
//    }

//    @Override
//    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        int position = viewHolder.getAdapterPosition();
//
//        View itemView = viewHolder.itemView;
//
//        final int SWIPE_THRESHOLD = itemView.getWidth() / 3;
//
//        if (position < SWIPE_THRESHOLD * 2) {
//            removeAction.accept(position);
//            adapter.notifyDataSetChanged();
//        }
//    }

//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX,
//                dY, actionState, isCurrentlyActive);
//
//        View itemView = viewHolder.itemView;
//
////        final int SWIPE_THRESHOLD = itemView.getWidth() / 3;
//
//        int backgroundCornerOffset = 20;
//        int alpha = Math.min(255, (int) Math.abs(dX) / 2);
//        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 4;
//        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
//        int iconBottom = iconTop + icon.getIntrinsicHeight();
//
//        int itemMiddleHeight = itemView.getHeight() / 2;
//
//        if (dX > 0) { // Swiping to the right
//            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
//            int iconRight = itemView.getLeft() + iconMargin;
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//
//            background.setBounds(itemView.getLeft(), itemView.getTop(),
//                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
//                    itemView.getBottom());
//        } else if (dX < 0) { // Swiping to the left
//
//            int iconOffset = icon.getIntrinsicWidth() - Math.min(icon.getIntrinsicWidth(), (int) Math.abs(dX) / 4);
//
//            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth() + iconOffset;
//            int iconRight = itemView.getRight() - iconMargin + iconOffset;
//
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//            icon.setAlpha(alpha);
//
//            background.setAlpha(alpha);
//            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
//                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
//
//        } else { // view is unSwiped
//            background.setBounds(0, 0, 0, 0);
//        }
//
//        background.draw(c);
//        icon.draw(c);
//    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((VH) viewHolder).getViewForeground();

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, @NonNull RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((VH) viewHolder).getViewForeground();
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((VH) viewHolder).getViewForeground();
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((VH) viewHolder).getViewForeground();

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
