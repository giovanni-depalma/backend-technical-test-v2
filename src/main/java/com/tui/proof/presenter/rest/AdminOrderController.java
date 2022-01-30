package com.tui.proof.presenter.rest;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.service.AdminOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "Order", description = "Endpoints for Admin to view and manage orders")
@SecurityRequirement(name = "secure-api")
@SecurityRequirement(name = "secure-api2")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("orders")
@AllArgsConstructor
public class AdminOrderController {
    private AdminOrderService orderService;

    @PostMapping("/findByCustomer")
    public List<Order> findByCustomer(@RequestBody PersonalInfo request) {
        return orderService.findByCustomer(request);
    }

}
