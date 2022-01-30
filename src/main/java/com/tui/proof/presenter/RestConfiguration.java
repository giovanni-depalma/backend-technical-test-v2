package com.tui.proof.presenter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tui.proof.domain.entities.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Configuration
@Slf4j
public class RestConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            DecimalFormat df = new DecimalFormat("#.00");
            @Override
            public void customize(
                    Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                //MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(Locale.ITALIAN);
                jacksonObjectMapperBuilder
                        .serializerByType(Money.class, new JsonSerializer<Money>() {
                            @Override
                            public void serialize(
                                    Money value, JsonGenerator gen, SerializerProvider serializers)
                                    throws IOException {
                                NumberFormat nf = NumberFormat.getCurrencyInstance();
                                gen.writeString(value == null ? "" : nf.format(value.getValue()));
                            }
                        }).
                        serializerByType(Instant.class, new JsonSerializer<Instant>() {
                            @Override
                            public void serialize(
                                    Instant value, JsonGenerator gen, SerializerProvider serializers)
                                    throws IOException {
                                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
                                Date date = new Date(value.getEpochSecond());
                                gen.writeString(value == null ? "" : f.format(date));
                            }
                        });

            }
        };
    }

}
