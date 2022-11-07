package com.geektrust.backend.service;

import com.geektrust.backend.dto.CollectionAmountDto;
import com.geektrust.backend.dto.PassengerTypeSummaryDto;
import com.geektrust.backend.exceptions.UserNotFoundException;
import java.util.List;

public interface ITrainSummaryService {
//    List<PassengerTypeSummaryDto> calculatePassengerTypeSummary() throws UserNotFoundException;
    List<CollectionAmountDto> calculateTotalAmount();
}
