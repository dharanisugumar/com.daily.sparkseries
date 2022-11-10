package com.geektrust.backend.dto;

import com.geektrust.backend.entities.PassengerTrip;
import com.geektrust.backend.enums.PassengerType;

public class PassengerTypeSummaryDto {
    private final PassengerType PassengerType;
    private final int passengerCount;

    public PassengerTypeSummaryDto(PassengerType PassengerType, int passengerCount) {
        this.PassengerType = PassengerType;
        this.passengerCount = passengerCount;
    }

    @Override
    public String toString() {
        return PassengerType + "  " + passengerCount;
    }
}
