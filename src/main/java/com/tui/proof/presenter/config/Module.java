package com.tui.proof.presenter.config;

import org.springframework.context.annotation.Configuration;

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
