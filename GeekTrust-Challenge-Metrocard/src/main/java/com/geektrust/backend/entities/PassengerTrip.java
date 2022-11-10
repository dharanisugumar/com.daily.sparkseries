package com.geektrust.backend.entities;

import com.geektrust.backend.enums.PassengerType;

import java.util.Objects;

public class PassengerTrip implements Comparable<PassengerTrip> {
    private final String fromStation;
    private final PassengerType passengerType;
    private int hashCode;


    public PassengerTrip(String fromStation, PassengerType passengerType){
        this.fromStation = fromStation;
        this.passengerType = passengerType;
        this.hashCode = Objects.hash(fromStation,passengerType);
    }

    @Override
    public String toString() {
        return //"\n" +
                " " + fromStation +
                " " + passengerType;
    }

    public String getFromStation() {
        return fromStation;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PassengerTrip that = (PassengerTrip) o;
        return fromStation.equals(that.fromStation) && passengerType == that.passengerType;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public int compareTo(PassengerTrip pt) {
        int retVal = getFromStation().compareTo(pt.fromStation);
//        System.out.println(retVal + " " +getFromStation()+ " " +pt.fromStation);
        int diff = getPassengerType().compareTo(pt.passengerType);

        if (retVal != 0) {
            return retVal;
        }else{
            return diff;
        }

    }

}
