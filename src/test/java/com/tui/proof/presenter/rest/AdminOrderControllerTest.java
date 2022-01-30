package com.tui.proof.presenter.rest;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AdminOrderController.class)
@WithMockUser(username="admin",roles={"ADMIN"})
public class AdminOrderControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderReader orderReader;


    @Test
    public void shouldFindAllOrders() throws Exception {

        int expectedSize = 2;
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderReader.findAll()).thenReturn(expected.stream());
        this.mockMvc.perform(get("/admin/orders")).andDo(print()).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrders(expected));
    }


    @Test
    public void shouldFindOrdersByCustomer() throws Exception {
        int expectedSize = 2;
        PersonalInfo request = FakeCustomer.buildPersonalInfo();
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderReader.findByCustomer(request)).thenReturn(expected.stream());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/admin/orders/findByCustomer").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrders(expected));
    }


    @Test
    public void shouldFindOrder() throws Exception {
        String id = "1";
        Order expected = FakeOrder.buildOrder();
        when(orderReader.findById(id)).thenReturn(Optional.of(expected));
        this.mockMvc.perform(get("/admin/orders/"+id)).andExpect(status().isOk())
                .andExpectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldNotFindCustomer() throws Exception {
        String id = "1";
        when(orderReader.findById(id)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/admin/orders/"+id)).andExpect(status().isNotFound());
    }
*/
}
