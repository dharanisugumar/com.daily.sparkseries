package com.geektrust.backend.config;

import com.geektrust.backend.commands.AddBalanceCommand;
import com.geektrust.backend.commands.AddCheckInCommand;
import com.geektrust.backend.commands.CommandInvoker;
import com.geektrust.backend.commands.PrintSummaryDetailsCommand;
import com.geektrust.backend.entities.MetroCardCheckIn;
import com.geektrust.backend.service.CollectionService;
import com.geektrust.backend.service.ITrainCollectionService;
import com.geektrust.backend.service.ITrainSummaryService;
import com.geektrust.backend.service.SummaryService;

public class ApplicationConfig {
    private final MetroCardCheckIn metroCardCheckInService =new MetroCardCheckIn();
    private final ITrainSummaryService trainSummaryService =new SummaryService(metroCardCheckInService);
    private final ITrainCollectionService trainCollectionService =new CollectionService(metroCardCheckInService);

    private final AddBalanceCommand balanceCommand=new AddBalanceCommand(trainCollectionService);
    private final AddCheckInCommand checkInCommand=new AddCheckInCommand(trainCollectionService);
    private final PrintSummaryDetailsCommand printSummaryCommand=new PrintSummaryDetailsCommand(trainSummaryService);
    private final CommandInvoker commandInvoker = new CommandInvoker();

    public CommandInvoker getCommandInvoker(){
        commandInvoker.register("BALANCE",balanceCommand);
        commandInvoker.register("CHECK_IN",checkInCommand);
        commandInvoker.register("PRINT_SUMMARY",printSummaryCommand);
        return commandInvoker;
    }
}
