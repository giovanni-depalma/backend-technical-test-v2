package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.mapper.*;
import com.tui.proof.presenter.serializer.MoneySerializer;
import com.tui.proof.service.CustomerService;
import com.tui.proof.service.OrderService;
import com.tui.proof.service.api.OrderRequest;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.tui.proof.presenter.Util.URI_ORDERS;

@WebMvcTest(OrderController.class)
@Import({OrderMapperImpl.class, CustomerMapperImpl.class, AddressMapperImpl.class, WebSecurityConfigParameters.class})
public class OrderControllerCreateUpdateTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;

    @Test
    public void shouldCreateOrder() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(customerService.findByEmail(request.customer().getEmail())).thenReturn(Optional.of(expected.getCustomer()));
        when(orderService.createOrder(request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(FakeOrder.buildResource(request));
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldCreateOrderWithNewCustomer() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(customerService.findByEmail(request.customer().getEmail())).thenReturn(Optional.empty());
        when(orderService.createOrder(request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(FakeOrder.buildResource(request));
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldNotCreateOrderWithBadData() throws Exception {
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateOrder() throws Exception {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(customerService.findByEmail(request.customer().getEmail())).thenReturn(Optional.of(expected.getCustomer()));
        when(orderService.updateOrder(id, request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put(URI_ORDERS+"/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andDo(print()).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldNotUpdateOrderWithBadData() throws Exception {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderService.updateOrder(id, request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put(URI_ORDERS+"/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateOrderNotPresent() throws Exception {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderService.updateOrder(id, request)).thenThrow(ItemNotFoundException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put(URI_ORDERS+"/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotUpdateOrderClosed() throws Exception {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderService.updateOrder(id, request)).thenThrow(EditingClosedOrderException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put(URI_ORDERS+"/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isConflict());
    }

    @Test
    public void shouldNotUpdateBadNumberOfPilotes() throws Exception {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderService.updateOrder(id, request)).thenThrow(BadPilotesOrderException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put(URI_ORDERS+"/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    private static class OrderMatcher {

        private static ResultMatcher[] checkOrder(Order order) {
            final String prefix = "";
            MoneySerializer serializer = new MoneySerializer();
            Customer customer = order.getCustomer();
            return new ResultMatcher[]{
                    jsonPath(prefix+"total").value(is(serializer.getString(order.getTotal())), String.class),
                    jsonPath(prefix+"pilotes").value(is(order.getPilotes()), Integer.class),
                    jsonPath(prefix+"createdAt").value(is(order.getCreatedAt().toString()), String.class),
                    jsonPath(prefix+"editableUntil").value(is(order.getEditableUntil().toString()), String.class),
                    jsonPath(prefix+"delivery.street").value(is(order.getDelivery().getStreet())),
                    jsonPath(prefix+"delivery.postcode").value(is(order.getDelivery().getPostcode())),
                    jsonPath(prefix+"delivery.city").value(is(order.getDelivery().getCity())),
                    jsonPath(prefix+"delivery.country").value(is(order.getDelivery().getCountry())),
                    jsonPath(prefix+"customer.email").value(is(customer.getEmail())),
                    jsonPath(prefix+"customer.firstName").value(is(customer.getFirstName())),
                    jsonPath(prefix+"customer.lastName").value(is(customer.getLastName())),
                    jsonPath(prefix+"customer.telephone").value(is(customer.getTelephone())),
                    jsonPath(prefix+"id").value(is(order.getId().toString()))
            };
        }
    }
}
