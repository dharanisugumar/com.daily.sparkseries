package com.geektrust.backend.entities.seniorcitizen;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.entities.TicketFare;

public class OnewaySeniorCitizenTicketFare extends TicketFare {
    public OnewaySeniorCitizenTicketFare(){
        super.price = Constants.SENIOR_CITIZEN_ONE_WAY;
    }
}
