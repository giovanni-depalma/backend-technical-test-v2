package com.tui.proof.presenter.config;

import com.tui.proof.core.domain.rules.OrderRules;
import com.tui.proof.core.gateway.TimerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class Module {
    /*
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {

            @Override
            public void customize(
                    Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(Locale.ITALIAN);
                jacksonObjectMapperBuilder
                        .serializerByType(Money.class, new JsonSerializer<Money>() {
                            @Override
                            public void serialize(
                                    Money value, JsonGenerator gen, SerializerProvider serializers)
                                    throws IOException {
                                System.out.println(value);
                                gen.writeString(format.format(value));
                            }
                        });
            }
        };
    }*/

}
