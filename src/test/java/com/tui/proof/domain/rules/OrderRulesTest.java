package com.tui.proof.domain.rules;

import com.tui.proof.domain.entities.base.Money;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class OrderRulesTest {

    private final Money priceForPilote = new Money(new BigDecimal("1.33"));
    private final Set<Integer> allowedPilotes = Set.of(5, 10, 15);
    private final long closeOrderAfterSeconds = 5*60;
    private final OrderRules rules = new OrderRules(priceForPilote, allowedPilotes,closeOrderAfterSeconds);


    @TestFactory
    public Stream<DynamicTest> shouldCalculateTotal() {
        return allowedPilotes.stream().map(pilotes -> DynamicTest.dynamicTest("Test Calculate Totals for pilots: " + pilotes, () -> {
            Money expected = new Money(priceForPilote.getValue().multiply(BigDecimal.valueOf(pilotes)));
            Money actual = rules.calculateTotal(pilotes);
            assertEquals(expected, actual);
        }));
    }

    @TestFactory
    public Stream<DynamicTest> shouldAllowedPilotes() {
        return allowedPilotes.stream().map(pilotes -> DynamicTest.dynamicTest("Test Allowed for pilots: " + pilotes, () -> {
            boolean actual = rules.allowedPilotes(pilotes);
            assertTrue(actual);
        }));
    }

    @TestFactory
    public Stream<DynamicTest> shouldNotAllowedPilotes() {
        return IntStream.range(0,100).filter(i -> !allowedPilotes.contains(i)).mapToObj(pilotes -> DynamicTest.dynamicTest("Test Not Allowed for pilots: " + pilotes, () -> {
            boolean actual = rules.allowedPilotes(pilotes);
            assertFalse(actual);
        }));
    }

    @Test
    public void shouldCalculateEditableUntil(){
        Instant now = Instant.now();
        Instant actual = rules.calculateEditableUntil(now);
        Instant expected = now.plusSeconds(closeOrderAfterSeconds);
        assertEquals(expected, actual);
    }

}
