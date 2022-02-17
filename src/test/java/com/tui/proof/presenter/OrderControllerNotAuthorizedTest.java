package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.config.WebSecurityConfig;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.mapper.OrderMapper;
import com.tui.proof.service.CustomerService;
import com.tui.proof.service.OrderService;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.tui.proof.presenter.Util.URI_ORDERS;
import static com.tui.proof.presenter.Util.URI_ORDERS_FIND_BY_CUSTOMER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebFluxTest(OrderController.class)
@WithMockUser(username="admin",roles={"CUSTOMER"})
@Import({WebSecurityConfig.class, WebSecurityConfigParameters.class})
public class OrderControllerNotAuthorizedTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private CustomerMapper customerMapper;

    @Test
    public void shouldNotFindOrdersByCustomer() throws Exception {
        int expectedSize = 2;
        Customer request = FakeCustomer.buildCustomer();
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderService.findByCustomer(request)).thenReturn(Flux.fromIterable(expected));
        this.webClient.post().uri(URI_ORDERS_FIND_BY_CUSTOMER)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeCustomer.buildResource(request)))
                .exchange()
                .expectStatus().isForbidden();
    }
}
