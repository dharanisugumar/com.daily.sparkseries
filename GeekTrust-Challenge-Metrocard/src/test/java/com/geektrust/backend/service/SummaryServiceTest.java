package com.geektrust.backend.service;

import com.geektrust.backend.dto.CollectionAmountDto;
import com.geektrust.backend.entities.MetroCardCheckIn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("summaryServiceTest")
@ExtendWith(MockitoExtension.class)
public class SummaryServiceTest {

    @Mock
    private SummaryService summaryServiceMock;

    @Test
    @DisplayName("should Generate total collection For Each Station")
    public void shouldDoTotalCollection(){
        //Arrange
        String corporationIsToBoreWellRatio="1:2";
        MetroCardCheckIn metroCardCheckIn = new MetroCardCheckIn();
        CollectionAmountDto collectionAmountDtoActualExpected=new CollectionAmountDto("CENTRAL",400 , 0);
        when(summaryServiceMock.calculateTotalAmount()).thenReturn((List<CollectionAmountDto>) collectionAmountDtoActualExpected);

        //Act
        CollectionAmountDto collectionAmountDtoActual = (CollectionAmountDto) summaryServiceMock.calculateTotalAmount();

        //Assert
        Assertions.assertEquals(collectionAmountDtoActualExpected,collectionAmountDtoActual);
        verify(summaryServiceMock,times(1)).calculateTotalAmount();
    }



}
