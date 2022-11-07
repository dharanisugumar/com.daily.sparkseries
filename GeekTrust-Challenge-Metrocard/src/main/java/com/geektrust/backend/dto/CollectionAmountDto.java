package com.geektrust.backend.dto;

import com.geektrust.backend.enums.PassengerTravelType;

public class CollectionAmountDto {
    private final PassengerTravelType passengerTravelType;
    private final Integer collectionAmount;
    private final Integer discount;

    public CollectionAmountDto(Integer collectionAmount, PassengerTravelType passengerTravelType, Integer discount) {
        this.collectionAmount = collectionAmount;
        this.passengerTravelType = passengerTravelType;
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "TOTAL_COLLECTION " +
                passengerTravelType +
                " " + collectionAmount +
                " " + discount;
    }
}
