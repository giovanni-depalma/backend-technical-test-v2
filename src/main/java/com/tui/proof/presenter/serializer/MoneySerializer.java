package com.tui.proof.presenter.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tui.proof.domain.entities.base.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

@JsonComponent
public class MoneySerializer extends JsonSerializer<Money> {

    public String getString(Money value){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.ITALY);
        return value == null ? "" : nf.format(value.getValue());
    }

    @Override
    public void serialize(Money value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(getString(value));
    }
}
