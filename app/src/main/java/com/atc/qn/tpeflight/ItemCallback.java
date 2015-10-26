package com.atc.qn.tpeflight;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.helper.ItemTouchHelper;

public class ItemCallback extends ItemTouchHelper.Callback {
    private final Adapter mAdapter;

    public interface MoveInterface {
        void onItemDismiss(int position);
    }

    public ItemCallback(AlarmAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public ItemCallback(FlightAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Enable drag and swipe in both directions
        final int dragFlags = 0;
        final int swipeFlags = ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        if (mAdapter instanceof FlightAdapter)
            ((FlightAdapter)mAdapter).onItemDismiss(viewHolder.getAdapterPosition());
        else if (mAdapter instanceof AlarmAdapter)
            ((AlarmAdapter)mAdapter).onItemDismiss(viewHolder.getAdapterPosition());

    }
}