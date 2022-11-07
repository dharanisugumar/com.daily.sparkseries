package com.geektrust.backend.dto;

import com.geektrust.backend.enums.PassengerCategory;

public class PassengerTypeSummaryDto {
    private final PassengerCategory passengerCategory;
    private final Integer passengerCount;

    public PassengerTypeSummaryDto(Integer passengerCount, PassengerCategory passengerCategory) {
        this.passengerCount = passengerCount;
        this.passengerCategory = passengerCategory;
    }

    @Override
    public String toString() {
        return "PASSENGER_TYPE_SUMMARY " +
                passengerCategory +
                " " + passengerCount;
    }
}
