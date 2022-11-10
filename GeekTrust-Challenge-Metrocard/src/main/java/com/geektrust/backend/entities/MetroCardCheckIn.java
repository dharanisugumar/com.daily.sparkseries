package com.geektrust.backend.entities;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.enums.PassengerType;

import java.util.*;

public class MetroCardCheckIn {
    private final Map<String,Integer> metroCardBalanceMap;
    private final Map<PassengerTrip,Integer> listOfPassengerTypeSummary;
    private final Map<String,Integer> passengerTripMap;
    private final Map<String,String> passengerTravelTypeMap;
    private final Map<String, List<Integer>> stationMap;
    private final Map<PassengerType,Integer> passengerTypeMap;
    private final Map<PassengerType,Integer> listOfPasSumary;

    public MetroCardCheckIn() {
        this.passengerTypeMap = new HashMap<>();
        this.listOfPassengerTypeSummary = new TreeMap<>();
        this.listOfPasSumary = new TreeMap<>();
        this.metroCardBalanceMap = new HashMap<>();
        this.passengerTripMap = new HashMap<>();
        this.passengerTravelTypeMap = new HashMap<>();
        this.stationMap = new HashMap<>();
    }

    public void addBalanceToCurrentUser(String metroCardNumber, Integer metroCardBalance){
        metroCardBalanceMap.put(metroCardNumber,metroCardBalance);
    }

    public void calculateTotalCollection(String metroCardNumber,String fromStation, PassengerType passengerType,TicketFare ticketFare,int noOfTrips) {
        int currentBalance = 0, existingBalance, serviceCharge = 0, discountedFare, tripCount , discount;
        if (passengerTripMap.containsKey(metroCardNumber)) {
            passengerTripMap.put(metroCardNumber,passengerTripMap.get(metroCardNumber)+1);
        }else{
            passengerTripMap.put(metroCardNumber,noOfTrips);
        }

        passengerTravelTypeMap.put(metroCardNumber, fromStation);
        //get trip count of passenger
        tripCount = passengerTripMap.get(metroCardNumber);
        // get the existing balance of the passenger
        existingBalance = metroCardBalanceMap.get(metroCardNumber);
        if (passengerTravelTypeMap.containsKey(metroCardNumber) && (tripCount %2==0)) {
            // set the passengertraveltype as return and update the balance
            // return journey apply 50% discount
            discountedFare = (int) (ticketFare.getPrice() * Constants.RETURN_DISCOUNT);
            discount = discountedFare;
        } else {
            // single trip
            discountedFare = ticketFare.getPrice();
            discount = 0;
        }
        if (existingBalance < discountedFare) {
            //recharge, calculate service fee
            serviceCharge = (int) ((discountedFare - existingBalance) * Constants.PENALTY);
            metroCardBalanceMap.put(metroCardNumber, currentBalance);
        } else { //has sufficient balance
            currentBalance = existingBalance - ticketFare.getPrice();
            // update current balance
            metroCardBalanceMap.put(metroCardNumber, currentBalance);
        }
        if (stationMap.containsKey(fromStation)){
            stationMap.put(fromStation, Arrays.asList(stationMap.get(fromStation).get(0)+discountedFare+serviceCharge,stationMap.get(fromStation).get(1)+discount));
        }else{
            stationMap.put(fromStation,Arrays.asList(discountedFare+serviceCharge,discount));
        }
    }

    public void addPassengerTravelType(String metroCardNumber,String fromStation, PassengerType passengerType,int noOfTrips) {
        if (listOfPassengerTypeSummary.containsKey(new PassengerTrip(fromStation,passengerType))){
            listOfPassengerTypeSummary.put(new PassengerTrip(fromStation,passengerType), listOfPassengerTypeSummary.getOrDefault(new PassengerTrip(fromStation,passengerType),noOfTrips)+1);
        }else{
            listOfPassengerTypeSummary.put(new PassengerTrip(fromStation,passengerType),noOfTrips);
        }

    }

        public void debitBalanceToCurrentUser(String metroCardNumber, String fromStation, PassengerType passengerType, TicketFare ticketFare, int noOfTrips){
        calculateTotalCollection(metroCardNumber,fromStation, passengerType, ticketFare,noOfTrips);
        addPassengerTravelType(metroCardNumber,fromStation, passengerType, noOfTrips);
    }

    public HashMap<String, List<Integer>> getListOfTotalCollection() {
        return new HashMap<>(stationMap);
    }

    public TreeMap<PassengerTrip,Integer> getListOfPassengerTypeSummary() {
        return new TreeMap<>(listOfPassengerTypeSummary);
    }

    public TreeMap<PassengerType,Integer> getListOfPasSumary() {
        return new TreeMap<>(listOfPasSumary);
    }
}
