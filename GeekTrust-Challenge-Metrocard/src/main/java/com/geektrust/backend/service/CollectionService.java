package com.geektrust.backend.service;

import com.geektrust.backend.entities.MetroCardCheckIn;
import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.entities.factory.CollectionFactoryProducer;
import com.geektrust.backend.enums.PassengerType;
import com.geektrust.backend.exceptions.AddBalanceFailedException;
import com.geektrust.backend.exceptions.AddCheckInFailedException;

public class CollectionService implements ITrainCollectionService {

    private final MetroCardCheckIn metroCardCheckin;

    public CollectionService(MetroCardCheckIn metroCardCheckin) {
        this.metroCardCheckin = metroCardCheckin;
    }

    @Override
    public void addBalanceForUser(String metroCardNumber, Integer metroCardBalance)throws AddBalanceFailedException {
        metroCardCheckin.addBalanceToCurrentUser(metroCardNumber,metroCardBalance);
    }

    @Override
    public void addCheckInForUser(String metroCardNumber, PassengerType passengerType, String fromStation)throws AddCheckInFailedException {
        TicketFare ticketFare= CollectionFactoryProducer.getPassengerType(passengerType);
        metroCardCheckin.debitBalanceToCurrentUser(metroCardNumber,fromStation,passengerType,ticketFare,1);

    }
}
