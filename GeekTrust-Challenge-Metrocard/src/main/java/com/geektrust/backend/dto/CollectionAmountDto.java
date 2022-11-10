package com.geektrust.backend.dto;

public class CollectionAmountDto {
    private final String fromStation;
    private final Integer collectionAmount;
    private final int discount;

    public CollectionAmountDto(String fromStation,Integer collectionAmount, int discount) {
        this.fromStation = fromStation;
        this.collectionAmount = collectionAmount;
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "TOTAL_COLLECTION  " +
                fromStation +
                "  " + collectionAmount +
                        "  " + discount ;
    }
}
