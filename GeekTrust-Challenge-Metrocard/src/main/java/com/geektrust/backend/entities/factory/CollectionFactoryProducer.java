package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.enums.PassengerType;
import com.geektrust.backend.exceptions.PassengerCategoryInvalidTypeException;
import com.geektrust.backend.exceptions.PassengerTravelTypeInvalidTypeException;

public class CollectionFactoryProducer {
    private static AbstractFactory getPassengerTypeFactory(PassengerType passengerType){
        switch (passengerType){
            case ADULT:return new AdultTicketFareFactory();
            case SENIOR_CITIZEN:return new SeniorCitizenTicketFareFactory();
            case KID:return new KidTicketFareFactory();
        }
        return null;
    }
    public static TicketFare getPassengerType(PassengerType passengerType){
        AbstractFactory abstractFactory= CollectionFactoryProducer.getPassengerTypeFactory(passengerType);
        if(abstractFactory==null) throw new PassengerCategoryInvalidTypeException("PassengerCategory is Invalid!");
        TicketFare ticketFare=abstractFactory.getTicketFare();
        if(ticketFare==null) throw new PassengerTravelTypeInvalidTypeException("PassengerTravelType is Invalid!");
        return ticketFare;
    }
}
