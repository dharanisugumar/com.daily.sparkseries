package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.entities.seniorcitizen.OnewaySeniorCitizenTicketFare;

public class SeniorCitizenTicketFareFactory extends AbstractFactory{
    @Override
    TicketFare getTicketFare() {
        return new OnewaySeniorCitizenTicketFare();
    }
}
