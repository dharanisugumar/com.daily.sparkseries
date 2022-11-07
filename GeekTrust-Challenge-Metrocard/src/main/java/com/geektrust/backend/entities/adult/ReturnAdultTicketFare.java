package com.geektrust.backend.entities.adult;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.entities.TicketFare;

public class ReturnAdultTicketFare extends TicketFare {
    public ReturnAdultTicketFare(){
        super.price = Constants.ADULT_RETURN;
    }
}
