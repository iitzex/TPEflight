package com.atc.qn.tpeflight;

public interface FlightInterface {
    void onFlightItemClick(Flight mInfo);
    void onStarClick(Flight mInfo);
    void removeTrackList(int position);
    int getTrackListSize();
    int getAlarmListSize();
}
