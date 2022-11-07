package com.geektrust.backend.service;

import com.geektrust.backend.entities.MetroCardCheckIn;
import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.entities.factory.TicketFareFactoryProducer;
import com.geektrust.backend.enums.PassengerCategory;
import com.geektrust.backend.enums.PassengerTravelType;
import com.geektrust.backend.exceptions.AddBalanceFailedException;
import com.geektrust.backend.exceptions.AddCheckInFailedException;
import java.util.HashMap;
import java.util.Map;

public class CollectionService implements ITrainCollectionService {

    private final MetroCardCheckIn metroCardCheckin;

    public CollectionService(MetroCardCheckIn metroCardCheckin) {
        this.metroCardCheckin = metroCardCheckin;
    }

    @Override
    public void addBalanceForUser(String card, Integer balance)throws AddBalanceFailedException {
        metroCardCheckin.addBalanceToCurrentUser(card,balance);
    }

    @Override
    public void addCheckInForUser(String card, PassengerCategory passengerCategory, PassengerTravelType passengerTravelType)throws AddCheckInFailedException {
        TicketFare ticketFare= TicketFareFactoryProducer.getPassengerTravelType(passengerCategory,passengerTravelType);
        metroCardCheckin.updateTravelCategoryToPassenger(passengerCategory,ticketFare);
        Map<PassengerTravelType, Integer> stationBalance = new HashMap<>();
//        stationBalance.put(station,ticketFare.getPrice());
        metroCardCheckin.debitBalanceToCurrentUser(card,ticketFare,stationBalance);
    }
}
