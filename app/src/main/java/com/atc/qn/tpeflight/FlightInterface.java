package com.atc.qn.tpeflight;

import java.util.ArrayList;

public interface FlightInterface {
    void onFlightItemClick(Flight mInfo);
    void onAlarmClick(Flight mInfo);
    void onStarClick(Flight mInfo);
    void removeTrackList(int position);
    int getTrackListSize();
    ArrayList<Flight> getAlarmList();
    int getAlarmListSize();
}
