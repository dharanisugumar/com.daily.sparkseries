package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.entities.adult.OnewayAdultTicketFare;

public class AdultTicketFareFactory extends AbstractFactory{
    @Override

    TicketFare getTicketFare() {
        return new OnewayAdultTicketFare();
    }
}
