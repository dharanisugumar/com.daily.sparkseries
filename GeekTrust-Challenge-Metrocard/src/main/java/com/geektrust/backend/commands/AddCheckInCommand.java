package com.geektrust.backend.commands;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.enums.PassengerType;
import com.geektrust.backend.enums.PassengerTravelType;
import com.geektrust.backend.exceptions.AddCheckInFailedException;
import com.geektrust.backend.service.ITrainCollectionService;
import java.util.List;

public class AddCheckInCommand implements ICommand{
    private final ITrainCollectionService checkInCollectionService;

    public AddCheckInCommand(ITrainCollectionService checkInCollectionService) {
        this.checkInCollectionService = checkInCollectionService;
    }

    @Override
    public void execute(List<String> tokens) {
        try {
            String metroCardNumber = String.valueOf(tokens.get(Constants.ONE));
            PassengerType passengerType = PassengerType.valueOf(tokens.get(Constants.TWO));
            String fromStation = String.valueOf(tokens.get(Constants.THREE));
            checkInCollectionService.addCheckInForUser(metroCardNumber,passengerType,fromStation);
        } catch (AddCheckInFailedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
