package com.geektrust.backend.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CollectionAmountDtoTest")
public class CollectionAmountDtoTest {
    @Test
    @DisplayName("should create a CollectionAmountDtoTest Object")
    public void shouldCreateAcollectionAmountDtoObject() {
        CollectionAmountDto collectionAmountDto=new CollectionAmountDto("CENTRAL",300,0);
        String expectedString="CENTRAL 300 0";
        String acutalString=collectionAmountDto.toString();
        assertEquals(expectedString,acutalString);
    }
}
