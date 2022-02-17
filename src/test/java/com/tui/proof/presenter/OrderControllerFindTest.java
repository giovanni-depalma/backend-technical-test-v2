package com.tui.proof.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.config.WebSecurityConfig;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.mapper.AddressMapperImpl;
import com.tui.proof.mapper.CustomerMapperImpl;
import com.tui.proof.mapper.OrderMapperImpl;
import com.tui.proof.presenter.api.OrderResource;
import com.tui.proof.presenter.serializer.MoneySerializer;
import com.tui.proof.service.CustomerService;
import com.tui.proof.service.OrderService;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.tui.proof.presenter.Util.URI_ORDERS;
import static com.tui.proof.presenter.Util.URI_ORDERS_FIND_BY_CUSTOMER;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@WebFluxTest(OrderController.class)
@WithMockUser(username = "admin", roles = {"ADMIN"})
@Import({OrderMapperImpl.class, AddressMapperImpl.class, CustomerMapperImpl.class, WebSecurityConfig.class, WebSecurityConfigParameters.class})
public class OrderControllerFindTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;


    @Test
    public void shouldFindOrdersByCustomer() throws Exception {
        int expectedSize = 2;
        Customer request = FakeCustomer.buildCustomer();
        List<Order> expected = FakeListBuilder.buildList(expectedSize, FakeOrder::buildOrder);
        when(orderService.findByCustomer(request)).thenReturn(Flux.fromIterable(expected));
        this.webClient.post().uri(URI_ORDERS_FIND_BY_CUSTOMER)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeCustomer.buildResource(request)))
                .exchange()
                //.expectBodyList(OrderResource.class).hasSize(expectedSize)
                .expectAll(OrderMatcher.checkOrders(expected));

    }

    private static class OrderMatcher {


        private static WebTestClient.ResponseSpec.ResponseSpecConsumer checkOrders(List<Order> orders) {
            MoneySerializer serializer = new MoneySerializer();
            return responseSpec -> {
                WebTestClient.BodyContentSpec body = responseSpec.expectBody();
                for (int i = 0; i < orders.size(); i++) {
                    String prefix = "$[" + i + "].";
                    Order order = orders.get(i);
                    Customer customer = order.getCustomer();
                    body
                            .jsonPath(prefix + "total").value(is(serializer.getString(order.getTotal())), String.class)
                            .jsonPath(prefix + "pilotes").value(is(order.getPilotes()), Integer.class)
                            .jsonPath(prefix + "createdAt").value(is(order.getCreatedAt().toString()), String.class)
                            .jsonPath(prefix + "editableUntil").value(is(order.getEditableUntil().toString()), String.class)
                            .jsonPath(prefix + "delivery.street").value(is(order.getDelivery().getStreet()))
                            .jsonPath(prefix + "delivery.postcode").value(is(order.getDelivery().getPostcode()))
                            .jsonPath(prefix + "delivery.city").value(is(order.getDelivery().getCity()))
                            .jsonPath(prefix + "delivery.country").value(is(order.getDelivery().getCountry()))
                            .jsonPath(prefix + "customer.email").value(is(customer.getEmail()))
                            .jsonPath(prefix + "customer.firstName").value(is(customer.getFirstName()))
                            .jsonPath(prefix + "customer.lastName").value(is(customer.getLastName()))
                            .jsonPath(prefix + "customer.telephone").value(is(customer.getTelephone()))
                            .jsonPath(prefix + "id").value(is(order.getId().toString()));
                }
            };
        }
    }
}
