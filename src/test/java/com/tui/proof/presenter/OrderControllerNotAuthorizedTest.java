package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.PersonalInfo;
import com.tui.proof.presenter.api.PurchaserOrderMapper;
import com.tui.proof.service.OrderService;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.tui.proof.presenter.Util.URI_ORDERS_FIND_BY_CUSTOMER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser(username="admin",roles={"CUSTOMER"})
@Import({PurchaserOrderMapper.class, WebSecurityConfigParameters.class})
public class OrderControllerNotAuthorizedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void shouldNotFindOrdersByCustomer() throws Exception {
        int expectedSize = 2;
        PersonalInfo request = FakeCustomer.buildPersonalInfo();
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderService.findByCustomer(request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post(URI_ORDERS_FIND_BY_CUSTOMER).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isForbidden());
    }
}
