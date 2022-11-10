package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.entities.kids.OnewayKidTicketFare;

public class KidTicketFareFactory extends AbstractFactory{
    @Override
    TicketFare getTicketFare() {
        return new OnewayKidTicketFare();
    }
}
