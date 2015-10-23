package com.atc.qn.tpeflight;

public interface FlightInterface {
    void onFlightItemClick(Flight mInfo);
    void onAlarmClick();
    void onStarClick(Flight mInfo);
    int getTrackListSize();
}
