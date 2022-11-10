package com.geektrust.backend.commands;

import com.geektrust.backend.dto.CollectionAmountDto;
import com.geektrust.backend.dto.PassengerTypeSummaryDto;
import com.geektrust.backend.exceptions.UserNotFoundException;
import com.geektrust.backend.service.ITrainSummaryService;

import java.util.List;

public class PrintSummaryDetailsCommand implements ICommand{

    private final ITrainSummaryService summaryService;

    public PrintSummaryDetailsCommand(ITrainSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @Override
    public void execute(List<String> tokens) {
        try {
            summaryService.calculateTotalAmount();
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
