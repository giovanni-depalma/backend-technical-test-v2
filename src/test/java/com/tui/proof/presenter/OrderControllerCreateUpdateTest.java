package com.tui.proof.presenter;

import com.tui.proof.config.WebSecurityConfig;
import com.tui.proof.config.WebSecurityConfigParameters;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.mapper.AddressMapperImpl;
import com.tui.proof.mapper.CustomerMapperImpl;
import com.tui.proof.mapper.OrderMapperImpl;
import com.tui.proof.presenter.serializer.MoneySerializer;
import com.tui.proof.service.OrderService;
import com.tui.proof.service.api.OrderRequest;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.tui.proof.presenter.Util.URI_ORDERS;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@WebFluxTest(OrderController.class)
@Import({OrderMapperImpl.class, CustomerMapperImpl.class, AddressMapperImpl.class, WebSecurityConfig.class, WebSecurityConfigParameters.class})
public class OrderControllerCreateUpdateTest {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OrderService orderService;


    @Test
    public void shouldCreateOrder(){
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderService.createOrder(request)).thenReturn(Mono.just(expected));
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isCreated()
                .expectAll(OrderMatcher.checkOrder(expected));
    }



    @Test
    public void shouldNotCreateOrderWithBadData(){
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        this.webClient.post().uri(URI_ORDERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void shouldUpdateOrder(){
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderService.updateOrder(id, request)).thenReturn(Mono.just(expected));
        this.webClient.put().uri(URI_ORDERS+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isOk()
                .expectAll(OrderMatcher.checkOrder(expected));
    }

    @Test
    public void shouldNotUpdateOrderWithBadData() {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        Order expected = FakeOrder.buildOrder();
        when(orderService.updateOrder(id, request)).thenReturn(Mono.just(expected));
        this.webClient.put().uri(URI_ORDERS+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void shouldNotUpdateOrderNotPresent(){
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderService.updateOrder(id, request)).thenReturn(Mono.error(new ItemNotFoundException()));
        this.webClient.put().uri(URI_ORDERS+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isNotFound();
    }

    @Test
    public void shouldNotUpdateOrderClosed(){
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderService.updateOrder(id, request)).thenReturn(Mono.error(new EditingClosedOrderException()));
        this.webClient.put().uri(URI_ORDERS+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void shouldNotUpdateBadNumberOfPilotes(){
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderService.updateOrder(id, request)).thenReturn(Mono.error(new BadPilotesOrderException()));
        this.webClient.put().uri(URI_ORDERS+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FakeOrder.buildResource(request)))
                .exchange().expectStatus().isBadRequest();
    }

    private static class OrderMatcher {

        private static  WebTestClient.ResponseSpec.ResponseSpecConsumer checkOrder(Order order) {
            final String prefix = "";
            MoneySerializer serializer = new MoneySerializer();
            Customer customer = order.getCustomer();
            return responseSpec -> {
                WebTestClient.BodyContentSpec body = responseSpec.expectBody();
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
            };

        }
    }
}
