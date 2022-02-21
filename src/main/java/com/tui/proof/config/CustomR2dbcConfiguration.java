package com.tui.proof.config;

import com.tui.proof.repository.converter.OrderReadConverter;
import com.tui.proof.repository.converter.OrderWriteConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomR2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:h2:mem:///pilotes");
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converterList = new ArrayList<>();
        converterList.add(new OrderReadConverter());
        converterList.add(new OrderWriteConverter());
        return converterList;
    }
}
