package com.geektrust.backend.entities.seniorcitizen;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.entities.TicketFare;

public class ReturnSeniorCitizenTicketFare extends TicketFare {
    public ReturnSeniorCitizenTicketFare(){
        super.price = Constants.SENIOR_CITIZEN_RETURN;
    }
}
