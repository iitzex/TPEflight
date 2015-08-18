package com.atc.qn.tpeflight;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightHolder>{
    private ArrayList<Flight> mFlightAll;
    private String mAction = "";

    public FlightAdapter(ArrayList<String> mFlightAllStr, String mAction) {
        mFlightAll = new ArrayList<>();
        this.mAction = mAction;
        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd");
        String dayStr = day.format(timeInst.getTime());
//        dayStr = "2015/08/16";

        for(String item : mFlightAllStr) {
            String[] info = item.split(",");

            Flight target = new Flight();
            target.setAirlines(info[2].trim());
            target.setAirlinesTW(info[3].trim());
            target.setFlightNO(info[4].trim());

            target.setAction(info[1].trim());
            target.setTerminal("第" + info[0].trim() + "航廈");

            target.setBay(info[5].trim());
            target.setExpectDay(info[6].trim());
            target.setExpectTime(info[7].trim().substring(0, 5));
            target.setActualDay(info[8].trim());
            target.setActualTime(info[9].trim().substring(0, 5));
//            target.setDestination(airlines[11].trim());
            target.setDestinationTW(info[12].trim());

            target.setStatus(info[13].trim());
            target.setType(info[14].trim());

            target.setBaggage(info[18].trim());
            target.setCounter(info[19].trim());

            if (target.getAction().equals(mAction) && target.getExpectDay().equals(dayStr))
                mFlightAll.add(target);
        }
    }

    public int getPosition(){
        Calendar timeInst = Calendar.getInstance();
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        String hourStr = hour.format(timeInst.getTime());
        LogD.out("time:" + hourStr);
        LogD.out(String.valueOf(mFlightAll.size()));

        int position = 0;
        for(Flight item : mFlightAll) {
            if(Integer.valueOf(hourStr) > Integer.valueOf(item.getExpectTime().substring(0, 2))) {
                position++;
            }
        }
        LogD.out("position " + position);
        return position;
    }

    @Override
    public FlightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_row, parent, false);
        FlightHolder holder = new FlightHolder(v);

        return holder;
    }

    public void setAirlineLogo(FlightHolder holder, Flight target){
        String IATA = target.getAirlines();
        if (IATA.equals("CI"))
            holder.mLogo.setImageResource(R.drawable.l_ci);
        else if (IATA.equals("BR"))
            holder.mLogo.setImageResource(R.drawable.l_br);
        else if (IATA.equals("GE"))
            holder.mLogo.setImageResource(R.drawable.l_ge);
        else if (IATA.equals("CX"))
            holder.mLogo.setImageResource(R.drawable.l_cx);
        else if (IATA.equals("KA"))
            holder.mLogo.setImageResource(R.drawable.l_ka);
        else if (IATA.equals("SQ"))
            holder.mLogo.setImageResource(R.drawable.l_sq);
        else if (IATA.equals("CZ"))
            holder.mLogo.setImageResource(R.drawable.l_cz);
        else if (IATA.equals("MM"))
            holder.mLogo.setImageResource(R.drawable.l_mm);
        else if (IATA.equals("IT"))
            holder.mLogo.setImageResource(R.drawable.l_it);
        else if (IATA.equals("MU"))
            holder.mLogo.setImageResource(R.drawable.l_mu);
        else if (IATA.equals("DG"))
            holder.mLogo.setImageResource(R.drawable.l_dg);
        else if (IATA.equals("SC"))
            holder.mLogo.setImageResource(R.drawable.l_sc);
        else if (IATA.equals("B7"))
            holder.mLogo.setImageResource(R.drawable.l_b7);
        else if (IATA.equals("9C"))
            holder.mLogo.setImageResource(R.drawable.l_9c);
        else if (IATA.equals("5J"))
            holder.mLogo.setImageResource(R.drawable.l_5j);
        else if (IATA.equals("3K"))
            holder.mLogo.setImageResource(R.drawable.l_3k);
        else if (IATA.equals("NH"))
            holder.mLogo.setImageResource(R.drawable.l_nh);
        else if (IATA.equals("MF"))
            holder.mLogo.setImageResource(R.drawable.l_mf);
        else if (IATA.equals("CA"))
            holder.mLogo.setImageResource(R.drawable.l_ca);
        else if (IATA.equals("AE"))
            holder.mLogo.setImageResource(R.drawable.l_ae);
        else if (IATA.equals("NX"))
            holder.mLogo.setImageResource(R.drawable.l_nx);
        else if (IATA.equals("ZV"))
            holder.mLogo.setImageResource(R.drawable.l_zv);
        else if (IATA.equals("TK"))
            holder.mLogo.setImageResource(R.drawable.l_tk);
        else if (IATA.equals("HX"))
            holder.mLogo.setImageResource(R.drawable.l_hx);
        else if (IATA.equals("KL"))
            holder.mLogo.setImageResource(R.drawable.l_kl);
        else if (IATA.equals("UN"))
            holder.mLogo.setImageResource(R.drawable.l_un);
        else if (IATA.equals("PG"))
            holder.mLogo.setImageResource(R.drawable.l_pg);
        else if (IATA.equals("QF"))
            holder.mLogo.setImageResource(R.drawable.l_qf);
        else if (IATA.equals("TZ"))
            holder.mLogo.setImageResource(R.drawable.l_tz);
        else if (IATA.equals("PR"))
            holder.mLogo.setImageResource(R.drawable.l_pr);
        else if (IATA.equals("HU"))
            holder.mLogo.setImageResource(R.drawable.l_hu);
        else if (IATA.equals("ZH"))
            holder.mLogo.setImageResource(R.drawable.l_zh);
        else if (IATA.equals("TG"))
            holder.mLogo.setImageResource(R.drawable.l_tg);
        else if (IATA.equals("KE"))
            holder.mLogo.setImageResource(R.drawable.l_ke);
        else if (IATA.equals("HA"))
            holder.mLogo.setImageResource(R.drawable.l_ha);
        else if (IATA.equals("VN"))
            holder.mLogo.setImageResource(R.drawable.l_vn);
        else if (IATA.equals("OK"))
            holder.mLogo.setImageResource(R.drawable.l_ok);
        else if (IATA.equals("D7"))
            holder.mLogo.setImageResource(R.drawable.l_d7);
        else if (IATA.equals("DL"))
            holder.mLogo.setImageResource(R.drawable.l_dl);
        else if (IATA.equals("EK"))
            holder.mLogo.setImageResource(R.drawable.l_ek);
        else if (IATA.equals("FE"))
            holder.mLogo.setImageResource(R.drawable.l_fe);
        else if (IATA.equals("MH"))
            holder.mLogo.setImageResource(R.drawable.l_mh);
        else if (IATA.equals("OZ"))
            holder.mLogo.setImageResource(R.drawable.l_oz);
        else if (IATA.equals("JL"))
            holder.mLogo.setImageResource(R.drawable.l_jl);
        else if (IATA.equals("GA"))
            holder.mLogo.setImageResource(R.drawable.l_ga);
        else if (IATA.equals("TR"))
            holder.mLogo.setImageResource(R.drawable.l_tr);
        else if (IATA.equals("3U"))
            holder.mLogo.setImageResource(R.drawable.l_3u);
        else if (IATA.equals("BX"))
            holder.mLogo.setImageResource(R.drawable.l_bx);
        else if (IATA.equals("HO"))
            holder.mLogo.setImageResource(R.drawable.l_ho);
        else if (IATA.equals("JW"))
            holder.mLogo.setImageResource(R.drawable.l_jw);
        else if (IATA.equals("UA"))
            holder.mLogo.setImageResource(R.drawable.l_ua);
        else if (IATA.equals("AA"))
            holder.mLogo.setImageResource(R.drawable.l_aa);
        else if (IATA.equals("AK"))
            holder.mLogo.setImageResource(R.drawable.l_ak);
        else if (IATA.equals("LH"))
            holder.mLogo.setImageResource(R.drawable.l_lh);
        else if (IATA.equals("VJ"))
            holder.mLogo.setImageResource(R.drawable.l_vj);
        else if (IATA.equals("7C"))
            holder.mLogo.setImageResource(R.drawable.l_7c);
        else
            holder.mLogo.setImageResource(R.drawable.ic_menu_check);
    }
    @Override
    public void onBindViewHolder(FlightHolder holder, int position) {
        Flight target = mFlightAll.get(position);
        LogD.out(String.valueOf(position));
        setAirlineLogo(holder, target);

        holder.mAirlines.setText(target.getAirlines());
        holder.mAirlines_TW.setText(target.getAirlinesTW());
        holder.mFlightNO.setText(target.getFlightNO());

        holder.mExpectTime.setText(target.getExpectTime());
//        holder.mActualTime.setText(target.getActualTime());
        holder.mDestinationTW.setText(target.getDestinationTW());

        if (mAction.equals("D")) {
            holder.mCounter.setText(target.getCounter());
            holder.mBaggage.setVisibility(View.GONE);
        }else if (mAction.equals("A")) {
            holder.mCounter.setVisibility(View.GONE);
            holder.mBaggage.setText(target.getBaggage());
        }

        holder.mBay.setText(target.getBay());
        holder.mTerminal.setText(target.getTerminal());

        holder.mType.setText(target.getType());

        //clean holder
        holder.mIconText.setText("");
        Paint paint = holder.mExpectTime.getPaint();
        paint.setFlags(0);
        paint.setAntiAlias(true);

        if (target.getStatus().contains("CANCELLED")) {
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mIconStaus.setImageResource(R.drawable.ic_cancel);
        }else if (target.getStatus().contains("DELAY")) {
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mIconText.setText(target.getActualTime());
            holder.mIconStaus.setImageResource(R.drawable.ic_delay);
        }else if (target.getStatus().contains("ON TIME")) {
            holder.mIconStaus.setImageResource(R.drawable.ic_ontime);
        }else if (target.getStatus().contains("SCHEDULE CHANGE")) {
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mIconText.setText(target.getActualTime());
            holder.mIconStaus.setImageResource(R.drawable.ic_delay);
        }else if (target.getStatus().contains("ARRIVED")) {
            holder.mIconStaus.setImageResource(R.drawable.v_landing);
        }else if (target.getStatus().contains("DEPARTED")) {
            holder.mIconStaus.setImageResource(R.drawable.v_takeoff);
        }
    }

    @Override
    public int getItemCount() {
        return mFlightAll == null ? 0 : mFlightAll.size();
    }

    public static class FlightHolder extends RecyclerView.ViewHolder {
        ImageView mLogo;
        TextView mAirlines, mAirlines_TW, mFlightNO, mShare;
        TextView mExpectDay, mExpectTime, mActualDay, mActualTime;
        TextView mBay, mTerminal, mDestination, mDestinationIATA, mDestinationTW;
        TextView mCounter, mBaggage, mStatus, mType;
        TextView mIconText;
        ImageView mIconStaus;
        LinearLayout mLayout;

        FlightHolder(View view) {
            super(view);
            mLogo = (ImageView) view.findViewById(R.id.logo);

            mAirlines = (TextView) view.findViewById(R.id.airlines);
            mAirlines_TW = (TextView) view.findViewById(R.id.airlines_TW);

            mFlightNO = (TextView) view.findViewById(R.id.flightNO);
            mExpectTime = (TextView) view.findViewById(R.id.expecttime);
            //mExpectDay = (TextView) view.findViewById(R.id.expectday);
//            mActualTime = (TextView) view.findViewById(R.id.actualtime);
            //mActualDay = (TextView) view.findViewById(R.id.actualday);
            //mDestination = (TextView) view.findViewById(R.id.destination);
            mDestinationTW = (TextView) view.findViewById(R.id.destinationTW);
            mShare = (TextView) view.findViewById(R.id.share);

            mCounter = (TextView) view.findViewById(R.id.counter);
            mBaggage = (TextView) view.findViewById(R.id.baggage);

            mBay = (TextView) view.findViewById(R.id.bay);
            mTerminal = (TextView) view.findViewById(R.id.terminal);

            mType = (TextView) view.findViewById(R.id.type);

            mIconText = (TextView) view.findViewById(R.id.icontext);
            mIconStaus = (ImageView) view.findViewById(R.id.iconstatus);
        }
    }
}
