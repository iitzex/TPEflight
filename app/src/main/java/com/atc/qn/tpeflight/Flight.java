package com.atc.qn.tpeflight;

import android.os.Parcel;
import android.os.Parcelable;

public class Flight implements Parcelable{
    private String action;
    private String flightNO;
    private String airlines;
    private String airlinesTW;
    private String gate;
    private String terminal;
    private String expectDay;
    private String expectTime;
    private String actualDay;
    private String actualTime;
    private String destination;
    private String destinationIATA;
    private String destinationTW;
    private String status;
    private String type;
    private String counter;
    private String baggage;
    private String alarmTag;
    private int key;

    public Flight() {
        super();
    }

    public Flight(Flight mInfo) {
        this.action = mInfo.action;
        this.flightNO = mInfo.flightNO;
        this.airlines = mInfo.airlines;
        this.airlinesTW = mInfo.airlinesTW;
        this.gate = mInfo.gate;
        this.terminal = mInfo.terminal;
        this.expectDay = mInfo.expectDay;
        this.expectTime = mInfo.expectTime;
        this.actualDay = mInfo.actualDay;
        this.actualTime = mInfo.actualTime;
        this.destination = mInfo.destination;
        this.destinationIATA = mInfo.destinationIATA;
        this.destinationTW = mInfo.destinationTW;
        this.status = mInfo.status;
        this.type = mInfo.type;
        this.counter= mInfo.counter;
        this.baggage = mInfo.baggage;
        this.alarmTag = mInfo.alarmTag;
        this.key = mInfo.key;
    }

    public String getAction() {
        return action;
    }

    public String getFlightNO() {
        return flightNO;
    }

    public String getAirlines() {
        return airlines;
    }

    public String getGate() {
        return gate;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getExpectDay() {
        return expectDay;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public String getActualDay() {
        return actualDay;
    }

    public String getActualTime() {
        return actualTime;
    }

    public String getAirlinesTW() {
        return airlinesTW;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestinationTW() {
        return destinationTW;
    }

    public String getStatus() {
        return status;
    }

    public String getCounter() {
        return counter;
    }

    public String getBaggage() {
        return baggage;
    }

    public String getAlarmTag() {
        return alarmTag;
    }

    public int getKey() {
        return key;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setFlightNO(String flightNO) {
        this.flightNO = flightNO;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public void setExpectDay(String expectDay) {
        this.expectDay = expectDay;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public void setActualDay(String actualDay) {
        this.actualDay = actualDay;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setAirlinesTW(String airlinesTW) {
        this.airlinesTW = airlinesTW;
    }

    public void setDestinationTW(String destinationTW) {
        this.destinationTW = destinationTW;
    }

    public void setDestinationIATA(String destinationIATA) {
        this.destinationIATA = destinationIATA;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }

    public void setAlarmTag(String alarmTag) {
        this.alarmTag = alarmTag;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(flightNO);
        dest.writeString(airlines);
        dest.writeString(airlinesTW);
        dest.writeString(gate);
        dest.writeString(terminal);
        dest.writeString(expectDay);
        dest.writeString(expectTime);
        dest.writeString(actualDay);
        dest.writeString(actualTime);
        dest.writeString(destination);
        dest.writeString(destinationIATA);
        dest.writeString(destinationTW);
        dest.writeString(status);
        dest.writeString(counter);
        dest.writeString(baggage);
        dest.writeString(alarmTag);
    }

    public static final Parcelable.Creator<Flight> CREATOR = new Parcelable.Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel source) {
            Flight mInfo = new Flight();
            mInfo.setAction(source.readString());
            mInfo.setFlightNO(source.readString());
            mInfo.setAirlines(source.readString());
            mInfo.setAirlinesTW(source.readString());
            mInfo.setGate(source.readString());
            mInfo.setTerminal(source.readString());
            mInfo.setExpectDay(source.readString());
            mInfo.setExpectTime(source.readString());
            mInfo.setActualDay(source.readString());
            mInfo.setActualTime(source.readString());
            mInfo.setDestination(source.readString());
            mInfo.setDestinationIATA(source.readString());
            mInfo.setDestinationTW(source.readString());
            mInfo.setStatus(source.readString());
            mInfo.setCounter(source.readString());
            mInfo.setBaggage(source.readString());
            mInfo.setAlarmTag(source.readString());

            return mInfo;
        }
        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };
}
