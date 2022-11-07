package com.geektrust.backend.commands;

import com.geektrust.backend.constants.Constants;
import com.geektrust.backend.exceptions.AddBalanceFailedException;
import com.geektrust.backend.service.ITrainCollectionService;

import java.util.List;

public class AddBalanceCommand implements ICommand {

    private final ITrainCollectionService checkInCollectionService;

    public AddBalanceCommand(ITrainCollectionService checkInCollectionService) {
        this.checkInCollectionService = checkInCollectionService;
    }

    @Override
    public void execute(List<String> tokens) {
        try {
            String card=String.valueOf(tokens.get(Constants.ONE));
            Integer balance=Integer.valueOf(tokens.get(Constants.TWO));
            checkInCollectionService.addBalanceForUser(card,balance);
        } catch (AddBalanceFailedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
