package com.geektrust.backend.entities;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.enums.PassengerCategory;
import com.geektrust.backend.enums.PassengerTravelType;

import java.util.Map;
import java.util.TreeMap;

public class MetroCardCheckIn {
    private  Map<String,Integer> cardBalance;
    private  Map<String, Integer> cardPassengerTypeBalance;
    private  Map<PassengerTravelType, Integer> travelTypeBalance;
    private  Map<PassengerCategory,TicketFare> listOfCategory;
    private PassengerTravelType passengerTravelType;
//    private final PassengerTravelType passengerTravelType;

   public MetroCardCheckIn(){}

    public MetroCardCheckIn(PassengerTravelType passengerTravelType) {
        this.passengerTravelType = passengerTravelType;
        this.travelTypeBalance = new TreeMap<PassengerTravelType,Integer>();
        this.listOfCategory = new TreeMap<PassengerCategory,TicketFare>();
        this.cardBalance = new TreeMap<String,Integer>();
        this.cardPassengerTypeBalance = new TreeMap<String,Integer>();
    }

    public void addBalanceToCurrentUser(String card, Integer balance){
        cardBalance.put(card,balance);
    }

    public void debitBalanceToCurrentUser(String card, TicketFare ticketFare, Map<PassengerTravelType,Integer> travelTypeBalance){ //PassengerCategory stationName,PassengerCategory JourneyCount){
        int remainingBalance,ticketAmount,passengerBalance,totalAmount,serviceFee=0;
        ticketAmount = ticketFare.getPrice();

        cardPassengerTypeBalance.put(card,ticketAmount);
        passengerBalance=cardBalance.get(card);

        if (passengerBalance < ticketAmount){ //50 < 200
            // do the recharge
            remainingBalance = ticketAmount - passengerBalance;
            serviceFee = (int) (remainingBalance * Constants.PENALTY);
            remainingBalance = 0;
            cardBalance.put(card,remainingBalance);
        }
        else{
            remainingBalance = passengerBalance - ticketAmount;
            cardBalance.put(card,remainingBalance);
        }

        for(Integer fare: cardPassengerTypeBalance.values()){
            ticketAmount+=fare;
        }
        totalAmount = serviceFee+ticketAmount;
        travelTypeBalance.put(passengerTravelType,totalAmount);
        ////        cardPassengerTypeBalance.put(card, Arrays.asList(passengerCategory,"ONE_WAY=" + stationName, JourneyCount+1));
//        // Checks if the user is already present and journet count as even mark as return
//            if (cardPassengerTypeBalance.containsKey(card)) && (cardPassengerTypeBalance.get(card).get(2))%2==0){
//                // set card as return journey
//                cardPassengerTypeBalance.put(card, Arrays.asList(passengerCategory,"RETURN=" + stationName, JourneyCount+1));
//            }
//            // "MC1" : List(adult,FROM = STATIONNAME,1)
//        currentBalanceOfUser = cardBalance.get(card);
//        if (currentBalanceOfUser < passengerCategory.getPrice()) { //50<200
//            remainingBalance = passengerCategory.getPrice() - currentBalanceOfUser;
//            newBalance = 0;
//            cardBalance.put(card,newBalance);
//            amount += remainingBalance * Constants.SERVICE_FEE;
//        }
//        else {
//            cardBalance.put(card, remainingBalance);
//        }
//        cardPassengerType.put(card,"TO")
//        cardPassengerType.put(card,"FROM");
//        find_one_way_return = cardPassengerType.get(card);
//        if find_one_way_return == "TO"{
//            cardBalance.get(card) - Constants.ADULT_RETURN
//        }
//        currentBalanceOfUser = cardBalance.get(card);
//
//        if currentBalanceOfUser < passengerCategory.getPrice(): //50<200
//        remainingBalance = passengerCategory.getPrice - currentBalanceOfUser;
////        remainingBalance = remainingBalance * Constants.SERVICE_FEE; //to br done in summaryservice
//        getTravelType = cardPassengerTypeBalance.get(card).get(0)(1); //returns from or to
//        if getTravelType =="ONE_WAY":
//            addTravelType(card,passengerCategory,getTravelType)
//        ///// get current balance of that card {"MC1": "ADULT", {FROM : "CENTRAL"},{TO: "AIPORT"} }
//        // if current balance < constants.adult.one_way
//
//        listOfBalance.put(card,balance);
    }

    public void updateTravelCategoryToPassenger(PassengerCategory passengerCategory, TicketFare ticketFare){
        listOfCategory.put(passengerCategory,ticketFare);
    }

    public Map<String, Integer> getTicketFare() {
        return new TreeMap<String, Integer>(cardPassengerTypeBalance);
    }

    public Map<String, Integer> getPassengerBalance() {
        return new TreeMap<String, Integer>(cardBalance);
    }

    public TreeMap<PassengerTravelType, Integer> getListOfTravelTypeBalance() {
        return new TreeMap<PassengerTravelType, Integer>(travelTypeBalance);
    }

    public Map<PassengerCategory, TicketFare> getListOfCategory() {
        return new TreeMap<PassengerCategory, TicketFare>(listOfCategory);
    }

//    public Map<PassengerCategory, Integer> getListOfPassengers() {
//        return new TreeMap<PassengerCategory, Integer>(listOfPassengers);
//    }
}
