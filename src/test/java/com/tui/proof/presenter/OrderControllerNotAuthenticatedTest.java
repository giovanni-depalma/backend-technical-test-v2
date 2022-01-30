package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.presenter.serializer.MoneySerializer;
import com.tui.proof.service.AdminOrderService;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminOrderController.class)
public class OrderControllerNotAuthenticatedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminOrderService orderService;

    @Test
    public void shouldNotFindOrdersByCustomer() throws Exception {
        int expectedSize = 2;
        PersonalInfo request = FakeCustomer.buildPersonalInfo();
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderService.findByCustomer(request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/orders/findByCustomer").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isUnauthorized());
    }
}
