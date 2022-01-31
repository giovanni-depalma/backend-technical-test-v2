package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.tui.proof.config.SecurityParameters;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.PersonalInfo;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.presenter.data.PurchaserOrderMapper;
import com.tui.proof.presenter.serializer.MoneySerializer;
import com.tui.proof.service.PurchaserOrderService;
import com.tui.proof.service.data.OrderRequest;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaserOrderController.class)
@Import({PurchaserOrderMapper.class, SecurityParameters.class})
public class PurchaserOrderControllerNotValidTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private PurchaserOrderService orderService;

    @Test
    public void shouldNotCreateOrderWithBadEmail() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.getCustomer().setEmail("bad");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/purchaserOrders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithEmptyName() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.getCustomer().setFirstName("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/purchaserOrders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithEmptyLastName() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Faker faker = new Faker();
            request.getCustomer().setLastName("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/purchaserOrders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithEmptyPhone() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Faker faker = new Faker();
        request.getCustomer().setTelephone("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/purchaserOrders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithBadPostCode() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Faker faker = new Faker();
        request.getDelivery().setPostcode("a1234");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/purchaserOrders").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }
}
