package com.geektrust.backend.service;

import com.geektrust.backend.dto.CollectionAmountDto;
import com.geektrust.backend.dto.PassengerTypeSummaryDto;
import com.geektrust.backend.entities.MetroCardCheckIn;
import com.geektrust.backend.entities.ObjectComparator;
import com.geektrust.backend.entities.PassengerTrip;
import com.geektrust.backend.enums.PassengerType;
import com.geektrust.backend.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class SummaryService implements ITrainSummaryService{

    private final MetroCardCheckIn metroCardCheckIn;
    public SummaryService(MetroCardCheckIn metroCardCheckIn) {
        this.metroCardCheckIn = metroCardCheckIn;
    }

    @Override
    public List<CollectionAmountDto> calculateTotalAmount() throws UserNotFoundException {
        int amount,discount;
        List<CollectionAmountDto> collectionAmountDtoList = new ArrayList<>();
        List<PassengerTypeSummaryDto> passengerTypeSummaryDtoList = new ArrayList<>();
        HashMap<String, List<Integer>> totalCollection = metroCardCheckIn.getListOfTotalCollection();

        TreeMap<String, List<Integer>> sorted = new TreeMap<>(new ObjectComparator());
        sorted.putAll(totalCollection);
        for (String stationName :sorted.keySet() ) {
            List<Integer> amountAndDisct = totalCollection.get(stationName);
            amount = amountAndDisct.get(0);
            discount = amountAndDisct.get(1);
            collectionAmountDtoList.add(new CollectionAmountDto(stationName, amount, discount));
            passengerTypeSummaryDtoList = calculatePassengerTypeSummary(stationName);
            List<PassengerTypeSummaryDto> finalPassengerTypeSummaryDtoList = passengerTypeSummaryDtoList;
            collectionAmountDtoList.forEach(collectDto -> {
                        System.out.println(collectDto.toString());
                        System.out.println("PASSENGER_TYPE_SUMMARY ");
                        finalPassengerTypeSummaryDtoList.forEach(passengerTypeSummaryDto -> System.out.println(passengerTypeSummaryDto.toString()));
            });
            collectionAmountDtoList.clear();
            passengerTypeSummaryDtoList.clear();
        }
        return collectionAmountDtoList;
    }

    @Override
    public List<PassengerTypeSummaryDto> calculatePassengerTypeSummary(String stationName) throws UserNotFoundException {
        List<PassengerTypeSummaryDto> passengerTypeSummaryDtoList = new ArrayList<>();
        PassengerType passengerType;
        TreeMap<PassengerTrip, Integer> passengerMap = metroCardCheckIn.getListOfPassengerTypeSummary();
//        System.out.println(passengerMap.keySet()+ ""+passengerMap.values());
//        TreeMap<PassengerType, Integer> passengerSumMap = metroCardCheckIn.getListOfPasSumary();
//        TreeMap<PassengerTrip, Integer> sorted = new TreeMap<>();
//        sorted.putAll(passengerMap);
//        for (PassengerTrip stationPassengerType : passengerMap.keySet()) {
//            passengerSumMap.put(stationPassengerType.getPassengerType(),passengerMap.get(stationPassengerType));
//        }
//        System.out.println(passengerSumMap.keySet()+ ""+passengerSumMap.values());
        for (PassengerTrip stationPassengerType : passengerMap.keySet()) {
            if (stationPassengerType.getFromStation().equals(stationName)) {
                int pasTypeAndTripCount = passengerMap.get(stationPassengerType);
//                passengerType  = passengerSumMap.get(stationPassengerType.getPassengerType());
//                System.out.println(passengerSumMap.firstKey());
                passengerTypeSummaryDtoList.add(new PassengerTypeSummaryDto(stationPassengerType.getPassengerType(), pasTypeAndTripCount));
            }
        }
        return passengerTypeSummaryDtoList;
    }
}
