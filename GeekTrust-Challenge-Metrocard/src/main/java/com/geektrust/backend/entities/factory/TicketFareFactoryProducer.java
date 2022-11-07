package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.enums.PassengerCategory;
import com.geektrust.backend.enums.PassengerTravelType;
import com.geektrust.backend.exceptions.PassengerCategoryInvalidTypeException;
import com.geektrust.backend.exceptions.PassengerTravelTypeInvalidTypeException;

public class TicketFareFactoryProducer {
    private static AbstractFactory getPassengerCategoryFactory(PassengerCategory passengerCategory){
        switch (passengerCategory){
            case ADULT:return new AdultTicketFareFactory();
            case SENIOR_CITIZEN:return new SeniorCitizenTicketFareFactory();
            case KID:return new KidTicketFareFactory();
        }
        return null;
    }
    public static TicketFare getPassengerTravelType(PassengerCategory passengerCategory, PassengerTravelType passengerTravelType){
        AbstractFactory abstractFactory=TicketFareFactoryProducer.getPassengerCategoryFactory(passengerCategory);
        if(abstractFactory==null) throw new PassengerCategoryInvalidTypeException("PassengerCategory is Invalid!");
        TicketFare travelFare=abstractFactory.getTravelCategory(passengerTravelType);
        if(travelFare==null) throw new PassengerTravelTypeInvalidTypeException("PassengerTravelType is Invalid!");
        return travelFare;
    }
}
