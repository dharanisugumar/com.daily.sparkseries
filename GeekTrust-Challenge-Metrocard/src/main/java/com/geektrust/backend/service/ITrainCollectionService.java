package com.geektrust.backend.service;

import com.geektrust.backend.enums.PassengerCategory;
import com.geektrust.backend.enums.PassengerTravelType;

public interface ITrainCollectionService {
    void addBalanceForUser(String metroCardNumber, Integer balanceInTheMetroCard);
    void addCheckInForUser(String metroCardNumber, PassengerCategory passengerType, PassengerTravelType station);
}
