package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.PersonalInfo;
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
@WithMockUser(username="admin",roles={"ADMIN"})
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminOrderService orderService;

    @Test
    public void shouldFindOrdersByCustomer() throws Exception {
        int expectedSize = 2;
        PersonalInfo request = FakeCustomer.buildPersonalInfo();
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderService.findByCustomer(request)).thenReturn(expected);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/orders/findByCustomer").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrders(expected));
    }


    private static class OrderMatcher {

        private static ResultMatcher[] checkOrders(List<Order> orders) {
            return IntStream.range(0, orders.size()).mapToObj(i -> checkOrder("_embedded.orders["+i+"].", orders.get(i))).flatMap(Arrays::stream).toArray(ResultMatcher[]::new);
        }


        private static ResultMatcher[] checkOrder(String prefix, Order order) {
            MoneySerializer serializer = new MoneySerializer();
            PersonalInfo personalInfo = order.getCustomer().getPersonalInfo();
            return new ResultMatcher[]{
                    jsonPath(prefix+"total").value(is(serializer.getString(order.getTotal())), String.class),
                    jsonPath(prefix+"pilotes").value(is(order.getPilotes()), Integer.class),
                    jsonPath(prefix+"createdAt").value(is(order.getCreatedAt().toString()), String.class),
                    jsonPath(prefix+"editableUntil").value(is(order.getEditableUntil().toString()), String.class),
                    jsonPath(prefix+"delivery.street").value(is(order.getDelivery().getStreet())),
                    jsonPath(prefix+"delivery.postcode").value(is(order.getDelivery().getPostcode())),
                    jsonPath(prefix+"delivery.city").value(is(order.getDelivery().getCity())),
                    jsonPath(prefix+"delivery.country").value(is(order.getDelivery().getCountry())),
                    jsonPath(prefix+"customer.personalInfo.email").value(is(personalInfo.getEmail())),
                    jsonPath(prefix+"customer.personalInfo.firstName").value(is(personalInfo.getFirstName())),
                    jsonPath(prefix+"customer.personalInfo.lastName").value(is(personalInfo.getLastName())),
                    jsonPath(prefix+"customer.personalInfo.telephone").value(is(personalInfo.getTelephone())),
                    jsonPath(prefix+"id").value(is(order.getId().toString()))
            };
        }
    }
}
