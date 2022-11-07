package com.geektrust.backend.entities.factory;

import com.geektrust.backend.entities.TicketFare;
import com.geektrust.backend.enums.PassengerTravelType;

public abstract class AbstractFactory {
    abstract TicketFare getTravelCategory(PassengerTravelType travelType);
}
