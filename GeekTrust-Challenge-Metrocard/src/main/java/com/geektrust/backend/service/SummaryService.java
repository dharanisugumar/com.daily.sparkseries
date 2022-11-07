package com.geektrust.backend.service;

import com.geektrust.backend.dto.CollectionAmountDto;
import com.geektrust.backend.entities.MetroCardCheckIn;
import com.geektrust.backend.enums.PassengerTravelType;
import com.geektrust.backend.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SummaryService implements ITrainSummaryService{

    private final MetroCardCheckIn metroCardCheckIn;
    public SummaryService(MetroCardCheckIn metroCardCheckIn) {
        this.metroCardCheckIn = metroCardCheckIn;
    }

    @Override
    public List<CollectionAmountDto> calculateTotalAmount() throws UserNotFoundException {
        Integer amount = 0;
        Integer remainingBalance;
        List<CollectionAmountDto> collectionAmountDtoList = new ArrayList<>();
        Map<String, Integer> passengerBalance = metroCardCheckIn.getPassengerBalance();
        Map<String, Integer> ticketAmount = metroCardCheckIn.getTicketFare();
        TreeMap<PassengerTravelType, Integer> stationBalance = metroCardCheckIn.getListOfTravelTypeBalance();

//        if (passengerBalance < ticketAmount){ //50 < 200
//            // do the recharge
//            remainingBalance = ticketAmount - passengerBalance;
//            cardBalance.put(card,remainingBalance);
//            serviceFee = (int) (remainingBalance * Constants.PENALTY);
//        }
//        else{
//            remainingBalance = passengerBalance - ticketAmount;
//            cardBalance.put(card,remainingBalance);
//        }
//
//        for(Integer fare: cardPassengerTypeBalance.values()){
//            amount+=fare;
//        }
//        totalAmount = serviceFee+amount;
//        travelTypeBalance.put(passengerTravelType,totalAmount);

        for (PassengerTravelType passengerTravelType : stationBalance.keySet()) {
            amount += stationBalance.get(passengerTravelType);
            collectionAmountDtoList.add(new CollectionAmountDto(amount, passengerTravelType, 0));
        }
        return collectionAmountDtoList;
    }

//    @Override
//    public List<PassengerTypeSummaryDto> calculatePassengerTypeSummary() {
//        Integer passengerCount = 0;
//        List<PassengerTypeSummaryDto> passengerTypeSummaryDtoList = new ArrayList<>();
//        Map<Card, PassengerCategory, Station> passengerMap = MetroCardCheckIn.getListOfSubscription();
//        for (PassengerCategory passengerCategory : passengerMap.keySet()) {
//            passengerCount += passengerMap.get(passengerCategory);
//            passengerTypeSummaryDtoList.add(new PassengerTypeSummaryDto(passengerCount, passengerCategory));
//        }
//        return passengerTypeSummaryDtoList;
//    }
}
