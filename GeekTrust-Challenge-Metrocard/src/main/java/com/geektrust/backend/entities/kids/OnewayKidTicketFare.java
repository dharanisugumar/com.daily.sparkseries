package com.geektrust.backend.entities.kids;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.entities.TicketFare;

public class OnewayKidTicketFare extends TicketFare {
    public OnewayKidTicketFare(){
        super.price = Constants.KID_ONE_WAY;
    }
}
