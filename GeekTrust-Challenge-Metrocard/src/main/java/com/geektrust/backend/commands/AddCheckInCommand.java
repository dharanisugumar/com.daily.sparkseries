package com.geektrust.backend.commands;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.enums.PassengerCategory;
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
            String card = String.valueOf(tokens.get(Constants.ONE));
            PassengerCategory passengerType = PassengerCategory.valueOf(tokens.get(Constants.TWO));
            PassengerTravelType passengerTravelType=PassengerTravelType.valueOf(tokens.get(Constants.THREE));
            checkInCollectionService.addCheckInForUser(card,passengerType,passengerTravelType);
        } catch (AddCheckInFailedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
