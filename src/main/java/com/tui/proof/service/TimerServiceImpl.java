package com.tui.proof.service;

import java.time.Instant;


import org.springframework.stereotype.Service;

@Service
public class TimerServiceImpl implements TimerService {

    @Override
    public Instant now() {
        return Instant.now();
    }
    
}
