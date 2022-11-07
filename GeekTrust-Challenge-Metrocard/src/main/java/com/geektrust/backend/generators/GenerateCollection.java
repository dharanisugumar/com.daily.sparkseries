package com.geektrust.backend.generators;

public class GenerateCollection {
    private Integer totalCollectionForEachCheckIn;
    private Integer totalCost;

    public GenerateCollection(Integer totalCollectionForEachCheckIn) {
        this.totalCollectionForEachCheckIn = totalCollectionForEachCheckIn;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return totalCollectionForEachCheckIn +" " + totalCost;
    }


}
