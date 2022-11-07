package com.geektrust.backend.entities.adult;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.entities.TicketFare;

public class OnewayAdultTicketFare extends TicketFare {
    public OnewayAdultTicketFare(){
        super.price = Constants.ADULT_ONE_WAY;
    }
}
