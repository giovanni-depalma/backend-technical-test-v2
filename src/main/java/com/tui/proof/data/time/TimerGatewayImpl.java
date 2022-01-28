package com.tui.proof.data.time;

import java.time.Instant;

import com.tui.proof.core.gateway.TimerGateway;

import org.springframework.stereotype.Service;

@Service
public class TimerGatewayImpl implements TimerGateway {

    @Override
    public Instant now() {
        return Instant.now();
    }
    
}
