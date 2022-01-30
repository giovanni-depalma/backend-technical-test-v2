package com.tui.proof.presenter.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tui.proof.domain.entities.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.NumberFormat;

@JsonComponent
public class MoneySerializer extends JsonSerializer<Money> {


    @Override
    public void serialize(Money value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        jsonGenerator.writeString(value == null ? "" : nf.format(value.getValue()));
    }
}
