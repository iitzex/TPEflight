package com.atc.qn.tpeflight;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
    private List<DrawerItem> mData;
    private DrawerCallback mDrawerCallbacks;
    private View mSelectedView;
    private int mSelectedPosition;

    public DrawerAdapter(List<DrawerItem> data) {
        mData = data;
    }

    public void setNavigationDrawerCallbacks(DrawerCallback drawerCallbacks) {
        mDrawerCallbacks = drawerCallbacks;
    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_row, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.itemView.setClickable(true);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if (mSelectedView != null) {
                               mSelectedView.setSelected(false);
                           }
                           mSelectedPosition = viewHolder.getAdapterPosition();
                           v.setSelected(true);
                           mSelectedView = v;
                           if (mDrawerCallbacks != null)
                               mDrawerCallbacks.onDrawerItemSelected(viewHolder.getAdapterPosition());
                       }
                   }
        );
        viewHolder.itemView.setBackgroundResource(R.drawable.row_selector);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(mData.get(i).getText());
        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mData.get(i).getDrawable(), null, null, null);

        if (mSelectedPosition == i) {
            if (mSelectedView != null) {
                mSelectedView.setSelected(false);
            }
            mSelectedView = viewHolder.itemView;
            mSelectedView.setSelected(true);
        }
    }

    public void selectPosition(int position) {
        mSelectedPosition = position;
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}