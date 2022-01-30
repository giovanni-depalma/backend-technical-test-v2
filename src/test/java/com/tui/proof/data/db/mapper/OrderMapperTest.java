package com.tui.proof.data.db.mapper;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.db.entities.CustomerDataOld;
import com.tui.proof.old.db.entities.OrderDataOld;
import com.tui.proof.old.db.mapper.CustomerMapper;
import com.tui.proof.old.db.mapper.OrderMapper;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class OrderMapperTest {

    @Test
    public void shouldMapToDomain() {
        CustomerMapper customerMapper = Mockito.mock(CustomerMapper.class);
        OrderMapper mapper = new OrderMapper(customerMapper);
        OrderDataOld orderData = FakeOrder.buildOrderData();
        PersonalInfo expectedPersonalInfo = FakeCustomer.buildPersonalInfo();
        when(customerMapper.toPersonalInfo(orderData.getCustomer())).thenReturn(expectedPersonalInfo);
        OrderOld actual = mapper.toDomain(orderData);
        assertAll(
                () -> assertEquals(orderData.getCreatedAt(), actual.getOrderSummary().getCreatedAt()),
                () -> assertEquals(orderData.getDeliveryCity(), actual.getDelivery().getCity()),
                () -> assertEquals(orderData.getDeliveryCountry(), actual.getDelivery().getCountry()),
                () -> assertEquals(orderData.getDeliveryPostcode(), actual.getDelivery().getPostcode()),
                () -> assertEquals(orderData.getDeliveryStreet(), actual.getDelivery().getStreet()),
                () -> assertEquals(orderData.getId(), Long.parseLong(actual.getId())),
                () -> assertEquals(orderData.getPilotes(), actual.getOrderSummary().getPilotes()),
                () -> assertEquals(orderData.getTotal(), actual.getOrderSummary().getTotal()),
                () -> assertEquals(orderData.getEditableUntil(), actual.getOrderSummary().getEditableUntil()),
                () -> assertEquals(expectedPersonalInfo, actual.getCustomer()));
    }

    @Test
    public void shouldPopulateData() {
        CustomerMapper customerMapper = Mockito.mock(CustomerMapper.class);
        OrderMapper mapper = new OrderMapper(customerMapper);
        OrderDataOld orderData = new OrderDataOld();
        CustomerDataOld customerData = FakeCustomer.buildCustomerData();
        OrderOld order = FakeOrder.buildOrder();
        mapper.populateData(orderData, order, customerData);
        assertAll(
                () -> assertEquals(order.getOrderSummary().getCreatedAt(), orderData.getCreatedAt()),
                () -> assertEquals(customerData, orderData.getCustomer()),
                () -> assertEquals(order.getDelivery().getCity(), orderData.getDeliveryCity()),
                () -> assertEquals(order.getDelivery().getCountry(), orderData.getDeliveryCountry()),
                () -> assertEquals(order.getDelivery().getPostcode(), orderData.getDeliveryPostcode()),
                () -> assertEquals(order.getDelivery().getStreet(), orderData.getDeliveryStreet()),
                () -> assertEquals(order.getOrderSummary().getPilotes(), orderData.getPilotes()),
                () -> assertEquals(order.getOrderSummary().getTotal(), orderData.getTotal()),
                () -> assertEquals(order.getOrderSummary().getEditableUntil(), orderData.getEditableUntil()));
    }

}
