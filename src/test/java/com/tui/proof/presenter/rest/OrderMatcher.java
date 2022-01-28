package com.tui.proof.presenter.rest;

import com.tui.proof.core.domain.data.Order;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class OrderMatcher {

    public static ResultMatcher[] checkOrders(List<Order> orders) {
        return IntStream.range(0, orders.size()).mapToObj(i -> checkOrder("$["+i+"].", orders.get(i))).flatMap(Arrays::stream).toArray(ResultMatcher[]::new);
    }

    public static ResultMatcher[] checkOrder(Order order) {
        return checkOrder("", order);
    }

    public static ResultMatcher[] checkOrder(String prefix, Order order) {
        return new ResultMatcher[]{
                jsonPath(prefix+"orderSummary.total").value(is(order.getOrderSummary().getTotal()), BigDecimal.class),
                jsonPath(prefix+"orderSummary.pilotes").value(is(order.getOrderSummary().getPilotes()), Integer.class),
                jsonPath(prefix+"orderSummary.createdAt").value(is(order.getOrderSummary().getCreatedAt().toString()), String.class),
                jsonPath(prefix+"orderSummary.editableUntil").value(is(order.getOrderSummary().getEditableUntil().toString()), String.class),
                jsonPath(prefix+"delivery.street").value(is(order.getDelivery().getStreet())),
                jsonPath(prefix+"delivery.postcode").value(is(order.getDelivery().getPostcode())),
                jsonPath(prefix+"delivery.city").value(is(order.getDelivery().getCity())),
                jsonPath(prefix+"delivery.country").value(is(order.getDelivery().getCountry())),
                jsonPath(prefix+"customer.email").value(is(order.getCustomer().getEmail())),
                jsonPath(prefix+"customer.firstName").value(is(order.getCustomer().getFirstName())),
                jsonPath(prefix+"customer.lastName").value(is(order.getCustomer().getLastName())),
                jsonPath(prefix+"customer.telephone").value(is(order.getCustomer().getTelephone())),
                jsonPath(prefix+"id").value(is(order.getId()))
        };
    }
}
