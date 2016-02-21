package com.atc.qn.tpeflight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightHolder>
        implements Filterable, ItemCallback.MoveInterface
{
    private ArrayList<Flight> mFlightAll = new ArrayList<>();
    private static ArrayList<Flight> mFlightDeparture = new ArrayList<>();
    private static ArrayList<Flight> mFlightArrival = new ArrayList<>();
    private ArrayList<Flight> mOrig;
    private String mAction = "";
    private Context mContext;
    private static int mClickPositionDeparture = -1;
    private static int mClickPositionArrival = -1;

    public FlightAdapter(ArrayList<Flight> mTracking, String mAction, Context mContext) {
        this.mAction = mAction;
        this.mContext = mContext;

        if (mAction.equals("TRACKING")) {
            mFlightAll = mTracking;
        }
    }

    public FlightAdapter(ArrayList<String> mFlightAllStr, boolean updated, String mAction, Context mContext) {
        this.mAction = mAction;
        this.mContext = mContext;

        if(updated) {
            mFlightArrival.clear();
            mFlightDeparture.clear();
            for (String item : mFlightAllStr) {
                String[] info = item.split(",");

                Flight target = new Flight();
                target.setAirlines(info[2].trim());
                target.setAirlinesTW(info[3].trim());
                target.setFlightNO(info[2].trim() + info[4].trim());
                target.setAction(info[1].trim());
                target.setTerminal("第" + info[0].trim() + "航廈");
                target.setGate(info[5].trim());
                target.setExpectDay(info[6].trim());
                target.setExpectTime(info[7].trim().substring(0, 5));
                target.setActualDay(info[8].trim());
                target.setActualTime(info[9].trim().substring(0, 5));
                target.setDestination(info[11].trim());
//                QNLog.d("flightadapter, " + target.getDestination());
                target.setDestinationTW(info[12].trim());
                target.setStatus(info[13].trim());
                target.setBaggage(info[18].trim());
                target.setCounter(info[19].trim());

                if (target.getAction().equals("A")) {
                    mFlightArrival.add(target);
                } else if (target.getAction().equals("D")) {
                    mFlightDeparture.add(target);
                }
            }
        }

        if (mAction.equals("A"))
            mFlightAll = mFlightArrival;
        else
            mFlightAll = mFlightDeparture;
    }

    public int getTimePosition(){
        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        String hourStr = hour.format(timeInst.getTime());

        int position = 0;
        for(Flight item : mFlightAll) {
            if(Integer.valueOf(hourStr) > Integer.valueOf(item.getExpectTime().substring(0, 2))) {
                position++;
            }
        }
        return position;
    }

    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
        ((FlightInterface)mContext).removeTrackList(position);

        String countMsg = "(" + mFlightAll.size() + ")";
        ((Activity) mContext).setTitle(mContext.getString(R.string.name_track) + countMsg);
    }

    @Override
    public FlightHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_row, parent, false);

        return new FlightHolder(v);
    }

    private void setLogo(FlightHolder holder, Flight target) {
        String iconName = "l_" + target.getAirlines().toLowerCase();
        int resId = mContext.getResources().getIdentifier(iconName, "drawable", mContext.getPackageName());
        try {
            holder.mLogo.setImageResource(resId);
        }catch (Exception e) {
            QNLog.d(iconName);
            QNLog.d(resId);
            QNLog.d(e.toString());
        }
    }

    @Override
    public void onBindViewHolder(FlightHolder holder, final int position) {
        final Flight target = mFlightAll.get(position);

        setLogo(holder, target);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FlightInterface) mContext).onFlightItemClick(target);
                if (mAction.equals("D"))
                    mClickPositionDeparture = position;
                else
                    mClickPositionArrival = position;
            }
        });

        holder.mAirlines_TW.setText(target.getAirlinesTW());
        holder.mFlightNO.setText(target.getFlightNO());

        holder.mExpectTime.setText(target.getExpectTime());
        holder.mDestinationTW.setText(target.getDestinationTW());

        if (mAction.equals("D")) {
            holder.mCounter.setText(target.getCounter());
            holder.mBaggage.setVisibility(View.GONE);
        }else if (mAction.equals("A")) {
            holder.mCounter.setVisibility(View.GONE);
            holder.mBaggage.setText(target.getBaggage());
        }

        holder.mGate.setText(target.getGate());
        holder.mTerminal.setText(target.getTerminal());

        //clean holder
        holder.mIconText.setText("");
        Paint paint = holder.mExpectTime.getPaint();
        paint.setFlags(0);
        paint.setAntiAlias(true);

        if (target.getStatus().contains("CANCELLED")) {
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mIconStatus.setImageResource(R.drawable.ic_cancel);
        }else if (target.getStatus().contains("DELAY")) {
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mIconText.setText(target.getActualTime());
            holder.mIconStatus.setImageResource(R.drawable.ic_delay);
        }else if (target.getStatus().contains("ON TIME")) {
            holder.mIconStatus.setImageResource(R.drawable.ic_ontime);
        }else if (target.getStatus().contains("SCHEDULE CHANGE")) {
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mIconText.setText(target.getActualTime());
            holder.mIconStatus.setImageResource(R.drawable.ic_delay);
        }else if (target.getStatus().contains("ARRIVED")) {
            holder.mIconStatus.setImageResource(R.drawable.v_landing);
        }else if (target.getStatus().contains("DEPARTED")) {
            holder.mIconStatus.setImageResource(R.drawable.v_takeoff);
        }
    }

    @Override
    public int getItemCount() {
        return mFlightAll == null ? 0 : mFlightAll.size();
    }

    @Override
    public Filter getFilter() {
        if (mOrig != null)
            mFlightAll = mOrig;

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterTxt = String.valueOf(constraint);
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Flight> results = new ArrayList<>();

                if (mOrig == null)
                    mOrig = mFlightAll;

                for (Flight item : mFlightAll) {
                    if (item.getAirlinesTW().contains(filterTxt) ||
                        item.getFlightNO().contains(filterTxt.toUpperCase()) ||
                        item.getDestinationTW().contains(filterTxt))
                        results.add(item);
                }

                oReturn.values = results;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFlightAll = (ArrayList<Flight>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class FlightHolder extends RecyclerView.ViewHolder
    {
        ImageView mLogo;
        TextView mAirlines_TW, mFlightNO, mExpectTime;
        TextView mGate, mTerminal, mDestinationTW;
        TextView mCounter, mBaggage;
        TextView mIconText;
        ImageView mIconStatus;

        FlightHolder(View view) {
            super(view);
            mLogo = (ImageView) view.findViewById(R.id.airlines_logo);
            mAirlines_TW = (TextView) view.findViewById(R.id.airlines_TW);

            mFlightNO = (TextView) view.findViewById(R.id.flightNO);
            mExpectTime = (TextView) view.findViewById(R.id.expecttime);
            mDestinationTW = (TextView) view.findViewById(R.id.destinationTW);

            mCounter = (TextView) view.findViewById(R.id.counter);
            mBaggage = (TextView) view.findViewById(R.id.baggage);
            mGate = (TextView) view.findViewById(R.id.gate);
            mTerminal = (TextView) view.findViewById(R.id.terminal);

            mIconText = (TextView) view.findViewById(R.id.icontext);
            mIconStatus = (ImageView) view.findViewById(R.id.iconstatus);
        }
    }
}


