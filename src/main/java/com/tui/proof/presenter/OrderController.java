package com.tui.proof.presenter;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.mapper.OrderMapper;
import com.tui.proof.presenter.api.CustomerResource;
import com.tui.proof.presenter.api.OrderRequestResource;
import com.tui.proof.presenter.api.OrderResource;
import com.tui.proof.service.CustomerService;
import com.tui.proof.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Endpoints for order management")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;

    @PostMapping
    @Operation(summary = "Create an order",  description = "Create an order", tags = {
            "Order"}, responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResource.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderResource> create(
            @Valid @RequestBody OrderRequestResource request)
            throws BadPilotesOrderException {
        return  orderService.createOrder(orderMapper.toDomain(request))
                .map(orderMapper::toResource);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Update an order (which has not yet been closed)", tags = {
            "Order"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResource.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Order not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Order closed", responseCode = "409", content = @Content)
    })
    public Mono<OrderResource> update(@PathVariable UUID id, @Valid @RequestBody OrderRequestResource request) throws BadPilotesOrderException, EditingClosedOrderException, ItemNotFoundException {
        return orderService.updateOrder(id, orderMapper.toDomain(request))
                .map(orderMapper::toResource);
    }

    @PostMapping("/findByCustomer")
    @SecurityRequirement(name = "secure-api")
    @SecurityRequirement(name = "secure-api2")
    public Flux<OrderResource> findByCustomer(@RequestBody CustomerResource request) {
        return orderService.findByCustomer(customerMapper.toDomain(request))
                .map(orderMapper::toResource);
    }


}
