package com.geektrust.backend.entities;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.enums.PassengerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MetroCardCheckInTest")
public class MetroCardCheckInTest {

    @Test
    @DisplayName("checkIfMetrocardBalance is assigning correct balance to the passenger")
    public void testCheckingAddBalanceToCurrentUser(){
        MetroCardCheckIn metroCardCheckIn=new MetroCardCheckIn();
        metroCardCheckIn.addBalanceToCurrentUser("MC1",400);
        assertEquals(400,metroCardCheckIn.getListOfTotalCollection());
    }
    @Test
    @DisplayName("should create a default MetroCard")
    public void shouldCreateADefaultMetroCard() {
        MetroCardCheckIn metroCardCheckIn = new MetroCardCheckIn();
        assertNotNull(metroCardCheckIn);
    }

    @Test
    @DisplayName("checkIfMetrocardBalance is debiting correct balance based on passenger checkin")
    public void testCheckingDebitBalanceToCurrentUser(){
        MetroCardCheckIn metroCardCheckIn=new MetroCardCheckIn();
        PassengerType passengerType = PassengerType.ADULT;
        TicketFare ticketFare = new TicketFare();
        metroCardCheckIn.debitBalanceToCurrentUser("MC1","CENTRAL",passengerType,ticketFare,1);
        assertEquals(200,metroCardCheckIn.getListOfTotalCollection());
    }
}
