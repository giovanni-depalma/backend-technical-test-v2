package com.tui.proof.data.time;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class TimerGatewayImplTest {
    
    @Test
    public void shouldReturnTime(){
        TimerGatewayImpl timerGateway = new TimerGatewayImpl();
        Instant before = Instant.now();
        Instant actual = timerGateway.now();
        Instant after = Instant.now();
        assertNotNull(actual);
      //  assertTrue(actual. >= before && actual <=after);
    }
}
