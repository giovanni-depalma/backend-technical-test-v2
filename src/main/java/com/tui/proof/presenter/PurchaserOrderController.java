package com.tui.proof.presenter;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.presenter.data.PurchaserOrderMapper;
import com.tui.proof.presenter.data.PurchaserOrder;
import com.tui.proof.service.data.OrderRequest;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.service.PurchaserOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/purchaserOrders")
@Tag(name = "Purchaser", description = "Endpoints for make and update an order")
@AllArgsConstructor
public class PurchaserOrderController {
    private final PurchaserOrderService purchaserOrderService;
    private final PurchaserOrderMapper mapper;

    @PostMapping
    @Operation(summary = "Create an order", description = "Create an order", tags = {
            "Order"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaserOrder.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
    })
    public EntityModel<PurchaserOrder> create(
            @Valid @RequestBody OrderRequest request)
            throws BadPilotesOrderException {
        Order order = purchaserOrderService.createOrder(request);
        return toEntityModel(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Update an order (which has not yet been closed)", tags = {
            "Order"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaserOrder.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Order not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Order closed", responseCode = "409", content = @Content)
    })
    public EntityModel<PurchaserOrder> update(@PathVariable UUID id, @Valid @RequestBody OrderRequest request) throws BadPilotesOrderException, EditingClosedOrderException, ItemNotFoundException {
        Order order = purchaserOrderService.updateOrder(id, request);
        return toEntityModel(order);
    }

    private EntityModel<PurchaserOrder> toEntityModel(Order order){
        return EntityModel.of(mapper.apply(order)).add(linkTo(PurchaserOrderController.class).slash(order.getId()).withSelfRel());
    }
}
