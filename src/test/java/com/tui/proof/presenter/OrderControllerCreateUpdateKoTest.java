package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.mapper.OrderMapper;
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

import static com.tui.proof.presenter.Util.URI_ORDERS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({WebSecurityConfigParameters.class})
public class OrderControllerCreateUpdateKoTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Test
    public void shouldNotCreateOrderWithBadEmail() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.customer().setEmail("bad");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithEmptyName() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.customer().setFirstName("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithEmptyLastName() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Faker faker = new Faker();
        request.customer().setLastName("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithEmptyPhone() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Faker faker = new Faker();
        request.customer().setTelephone("");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateOrderWithBadPostCode() throws Exception {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Faker faker = new Faker();
        request.delivery().setPostcode("a1234");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isBadRequest());
    }
}
