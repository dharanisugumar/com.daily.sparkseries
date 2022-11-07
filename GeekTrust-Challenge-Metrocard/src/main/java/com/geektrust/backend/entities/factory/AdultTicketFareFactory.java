package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.entities.adult.OnewayAdultTicketFare;
import com.geektrust.backend.entities.adult.ReturnAdultTicketFare;
import com.geektrust.backend.enums.PassengerTravelType;

public class AdultTicketFareFactory extends AbstractFactory{
    @Override
    TicketFare getTravelCategory(PassengerTravelType travelCategory) {
        switch (travelCategory){
            case CENTRAL:return new OnewayAdultTicketFare();
            case AIRPORT:return new ReturnAdultTicketFare();
        }
        return null;
    }
}
