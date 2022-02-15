package com.tui.proof.presenter;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.presenter.api.CustomerResource;
import com.tui.proof.presenter.api.OrderRequestResource;
import com.tui.proof.presenter.api.OrderResource;
import com.tui.proof.mapper.OrderMapper;
import com.tui.proof.service.CustomerService;
import com.tui.proof.service.OrderService;
import com.tui.proof.domain.exception.ItemNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Endpoints for order management")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;

    @PostMapping
    @Operation(summary = "Create an order", description = "Create an order", tags = {
            "Order"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResource.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
    })
    public EntityModel<OrderResource> create(
            @Valid @RequestBody OrderRequestResource request)
            throws BadPilotesOrderException {
        Customer customer = mapCustomer(request.customer());
        Order order = orderService.createOrder(orderMapper.toDomain(request, customer));
        return toEntityModel(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Update an order (which has not yet been closed)", tags = {
            "Order"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResource.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Order not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Order closed", responseCode = "409", content = @Content)
    })
    public EntityModel<OrderResource> update(@PathVariable UUID id, @Valid @RequestBody OrderRequestResource request) throws BadPilotesOrderException, EditingClosedOrderException, ItemNotFoundException {
        Customer customer = mapCustomer(request.customer());
        Order order = orderService.updateOrder(id, orderMapper.toDomain(request, customer));
        return toEntityModel(order);
    }

    @PostMapping("/findByCustomer")
    @SecurityRequirement(name = "secure-api")
    @SecurityRequirement(name = "secure-api2")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public CollectionModel<EntityModel<Order>> findByCustomer(@RequestBody CustomerResource request) {
        return CollectionModel.of(orderService.findByCustomer(customerMapper.toDomain(request)).stream().map(order -> {
            Link selfLink = linkTo(OrderController.class).slash(order.getId()).withSelfRel();
            return EntityModel.of(order).add(selfLink);
        }).toList());
    }

    private EntityModel<OrderResource> toEntityModel(Order order){
        return EntityModel.of(orderMapper.toResource(order)).add(linkTo(OrderController.class).slash(order.getId()).withSelfRel());
    }

    private Customer mapCustomer(CustomerResource resource){
        Customer target = customerService.findByEmail(resource.email()).orElseGet(Customer::new);
        customerMapper.update(resource, target);
        return target;
    }
}
