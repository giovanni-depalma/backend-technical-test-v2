package com.tui.proof.presenter;

import com.tui.proof.config.WebSecurityConfig;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.mapper.OrderMapper;
import com.tui.proof.service.CustomerService;
import com.tui.proof.service.OrderService;
import com.tui.proof.service.api.OrderRequest;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.tui.proof.presenter.Util.URI_ORDERS;

@WebFluxTest(OrderController.class)
@Import({WebSecurityConfig.class, WebSecurityConfigParameters.class})
public class OrderControllerCreateUpdateKoTest {
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
    public void shouldNotCreateOrderWithBadEmail(){
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.customer().setEmail("bad");
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateOrderWithEmptyName(){
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.customer().setFirstName("");
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateOrderWithEmptyLastName(){
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.customer().setLastName("");
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateOrderWithEmptyPhone(){
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.customer().setTelephone("");
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotCreateOrderWithBadPostCode() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        request.delivery().setPostcode("a1234");
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
