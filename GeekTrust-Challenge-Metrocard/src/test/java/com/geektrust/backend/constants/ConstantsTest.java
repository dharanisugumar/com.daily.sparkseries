package com.geektrust.backend.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ConstantsTest")
public class ConstantsTest {

    @Test
    @DisplayName("Testing Constants Value")
    public void testCheckingConstantsValue(){
        assertEquals(java.util.Optional.of(200),Constants.ADULT_ONE_WAY );
        assertEquals(java.util.Optional.of(100),Constants.SENIOR_CITIZEN_ONE_WAY);
        assertEquals(java.util.Optional.of(50),Constants.KID_ONE_WAY );
    }
}
