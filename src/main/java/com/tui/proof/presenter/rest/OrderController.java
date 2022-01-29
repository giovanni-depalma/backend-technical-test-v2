package com.tui.proof.presenter.rest;

import javax.validation.Valid;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
import com.tui.proof.core.service.OrderCreator;
import com.tui.proof.core.service.OrderUpdater;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order", description = "Endpoints for make an order")
@AllArgsConstructor
public class OrderController {
    private final OrderCreator orderCreator;
    private final OrderUpdater orderUpdater;

    @PostMapping
    @Operation(summary = "Create an order", description = "Create an order", tags = {
            "Order" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
            })
    public Order create(
            @Valid @RequestBody OrderRequest request)
            throws ItemNotFoundException {
        return orderCreator.createOrder(request);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable String id, @Valid @RequestBody OrderRequest request) {
        return orderUpdater.updateOrder(id, request);
    }

}
