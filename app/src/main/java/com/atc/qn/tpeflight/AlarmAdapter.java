package com.atc.qn.tpeflight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder>
    implements ItemCallback.MoveInterface
{
    private Context mContext;
    private ArrayList<Flight> mAlarmList;

    public AlarmAdapter(ArrayList<Flight> mAlarmList, Context mContext) {
        this.mContext = mContext;
        this.mAlarmList = mAlarmList;
    }

    @Override
    public AlarmHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_row, parent, false);

        return new AlarmHolder(v);
    }

    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
        ((FlightInterface)mContext).removeAlarmList(position);
    }

    @Override
    public int getItemCount() {
        return mAlarmList == null ? 0 : mAlarmList.size();
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, final int position) {
        final Flight target = mAlarmList.get(position);

        setLogo(holder, target);
        holder.mAirlines_TW.setText(target.getAirlinesTW());
        holder.mFlightNO.setText(target.getFlightNO());
        holder.mActualTime.setText(target.getActualTime());

        String ringTime = target.getAlarm();
        holder.mRingTime.setText(ringTime);

//        if (mAction.equals("D")) {
//            holder.mCounter.setText(target.getCounter());
//            holder.mBaggage.setVisibility(View.GONE);
//        }else if (mAction.equals("A")) {
//            holder.mCounter.setVisibility(View.GONE);
//            holder.mBaggage.setText(target.getBaggage());
//        }

        holder.mGate.setText(target.getGate());
        holder.mTerminal.setText(target.getTerminal());

    }

    public void setLogo(AlarmHolder holder, Flight target) {
        String iconName = "l_" + target.getAirlines().toLowerCase();
        int resId = mContext.getResources().getIdentifier(iconName, "drawable", mContext.getPackageName());
        holder.mLogo.setImageResource(resId);
    }

    public static class AlarmHolder extends RecyclerView.ViewHolder
    {
        ImageView mLogo;
        TextView mAirlines_TW, mFlightNO, mActualTime;
        TextView mRingTime;
        TextView mGate, mTerminal, mDestinationTW;
        TextView mCounter, mBaggage;

        AlarmHolder(View view) {
            super(view);
            mLogo = (ImageView) view.findViewById(R.id.alarm_logo);
            mAirlines_TW = (TextView) view.findViewById(R.id.airlines_TW);
            mFlightNO = (TextView) view.findViewById(R.id.flightNO);
            mActualTime = (TextView) view.findViewById(R.id.alarm_actualTime);
            mRingTime = (TextView) view.findViewById(R.id.alarm_ringTime);

            mDestinationTW = (TextView) view.findViewById(R.id.destinationTW);
            mCounter = (TextView) view.findViewById(R.id.counter);
            mBaggage = (TextView) view.findViewById(R.id.baggage);
            mGate = (TextView) view.findViewById(R.id.gate);
            mTerminal = (TextView) view.findViewById(R.id.terminal);
        }
    }
}
