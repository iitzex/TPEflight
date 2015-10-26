package com.atc.qn.tpeflight;

public interface FlightInterface {
    void onFlightItemClick(Flight mInfo);
    void onAlarmClick(Flight mInfo, boolean[] timeTable);
    void onStarClick(Flight mInfo);
    void removeTrackList(int position);
    int getTrackListSize();
    int getAlarmListSize();
}
