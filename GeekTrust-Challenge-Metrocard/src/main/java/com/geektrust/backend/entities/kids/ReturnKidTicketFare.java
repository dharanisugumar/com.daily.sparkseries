package com.geektrust.backend.entities.kids;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.entities.TicketFare;

public class ReturnKidTicketFare extends TicketFare {
    public ReturnKidTicketFare(){
        super.price = Constants.KID_RETURN;
    }
}
