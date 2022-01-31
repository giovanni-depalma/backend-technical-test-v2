package com.tui.proof.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class TimerServiceImplTest {
    
    @Test
    public void shouldReturnValidTime(){
        TimerServiceImpl timerService = new TimerServiceImpl();
        Instant before = Instant.now();
        Instant actual = timerService.now();
        Instant after = Instant.now();
        assertNotNull(actual);
        assertTrue(actual.compareTo(before) >= 0);
        assertTrue(actual.compareTo(after) <= 0);
    }

}
