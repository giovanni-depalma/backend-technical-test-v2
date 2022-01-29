package com.tui.proof.presenter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.domain.exception.BadPilotesOrderException;
import com.tui.proof.core.domain.exception.EditingClosedOrderException;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
import com.tui.proof.core.service.OrderCreator;
import com.tui.proof.core.service.OrderUpdater;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser(username="admin",roles={"ADMIN"})
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderCreator orderCreator;

    @MockBean
    private OrderUpdater orderUpdater;

    @Test
    public void shouldCreateOrder() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderCreator.createOrder(request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldNotCreateOrderWithBadData() throws Exception {
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateOrder() throws Exception {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderUpdater.updateOrder(id, request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put("/orders/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldNotUpdateOrderWithBadData() throws Exception {
        String id = "1";
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderUpdater.updateOrder(id, request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put("/orders/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateOrderNotPresent() throws Exception {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderUpdater.updateOrder(id, request)).thenThrow(ItemNotFoundException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put("/orders/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotUpdateOrderClosed() throws Exception {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderUpdater.updateOrder(id, request)).thenThrow(EditingClosedOrderException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put("/orders/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isForbidden());
    }

    @Test
    public void shouldNotUpdateBadNumberOfPilotes() throws Exception {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderUpdater.updateOrder(id, request)).thenThrow(BadPilotesOrderException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(put("/orders/"+id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }
}
