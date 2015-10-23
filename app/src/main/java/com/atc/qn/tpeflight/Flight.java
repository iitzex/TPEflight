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

    public Flight() {
        super();
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

    public String getDestination() {
        return destination;
    }

    public String getDestinationIATA() {
        return destinationIATA;
    }

    public String getAirlinesTW() {
        return airlinesTW;
    }

    public String getDestinationTW() {
        return destinationTW;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getCounter() {
        return counter;
    }

    public String getBaggage() {
        return baggage;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
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
        dest.writeString(type);
        dest.writeString(counter);
        dest.writeString(baggage);
    }

    public static final Parcelable.Creator<Flight> CREATOR = new Parcelable.Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel source) {
            Flight mFlight = new Flight();
            mFlight.setAction(source.readString());
            mFlight.setFlightNO(source.readString());
            mFlight.setAirlines(source.readString());
            mFlight.setAirlinesTW(source.readString());
            mFlight.setGate(source.readString());
            mFlight.setTerminal(source.readString());
            mFlight.setExpectDay(source.readString());
            mFlight.setExpectTime(source.readString());
            mFlight.setActualDay(source.readString());
            mFlight.setActualTime(source.readString());
            mFlight.setDestination(source.readString());
            mFlight.setDestinationIATA(source.readString());
            mFlight.setDestinationTW(source.readString());
            mFlight.setStatus(source.readString());
            mFlight.setType(source.readString());
            mFlight.setCounter(source.readString());
            mFlight.setBaggage(source.readString());

            return mFlight;
        }
        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };
}
