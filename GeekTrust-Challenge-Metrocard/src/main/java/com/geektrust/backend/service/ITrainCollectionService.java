package com.geektrust.backend.service;

import com.geektrust.backend.enums.PassengerType;

public interface ITrainCollectionService {
    void addBalanceForUser(String metroCardNumber, Integer balanceInTheMetroCard);
    void addCheckInForUser(String metroCardNumber, PassengerType passengerType, String fromStation);
}
