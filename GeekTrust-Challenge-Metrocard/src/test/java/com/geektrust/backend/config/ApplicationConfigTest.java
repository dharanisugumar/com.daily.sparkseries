package com.geektrust.backend.config;

import com.geektrust.backend.commands.CommandInvoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("ApplicationConfigTest")
public class ApplicationConfigTest {
    private ApplicationConfig applicationConfig;

    @BeforeEach
    void setup(){
        applicationConfig=new ApplicationConfig();
    }

    @Test
    @DisplayName("should Return CommandInvoker Via GetCommandInvoker()")
    public void shouldReturnCommandInvokerViaGetCommandInvoker() {
        CommandInvoker commandInvoker=applicationConfig.getCommandInvoker();
        assertNotNull(commandInvoker);
    }
}
