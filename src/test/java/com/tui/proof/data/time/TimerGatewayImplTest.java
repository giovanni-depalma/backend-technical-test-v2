package com.tui.proof.data.time;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import com.tui.proof.service.TimerServiceImpl;
import org.junit.jupiter.api.Test;

public class TimerGatewayImplTest {
    
    @Test
    public void shouldReturnValidTime(){
        TimerServiceImpl timerGateway = new TimerServiceImpl();
        Instant before = Instant.now();
        Instant actual = timerGateway.now();
        Instant after = Instant.now();
        assertNotNull(actual);
        assertTrue(actual.compareTo(before) >= 0);
        assertTrue(actual.compareTo(after) <= 0);
    }

}
