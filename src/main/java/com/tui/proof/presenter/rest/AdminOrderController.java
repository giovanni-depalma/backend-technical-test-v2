package com.tui.proof.presenter.rest;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
import com.tui.proof.core.service.OrderReader;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/orders")
@AllArgsConstructor
public class AdminOrderController {
    private final OrderReader orderReader;

    @GetMapping
    public Stream<Order> findAll(Principal principal) {
        System.out.println(principal);

        Authentication authentication = (Authentication) principal;
        /*
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt);
        System.out.println(jwt.getClaims());
        Map<String, ?> map = jwt.getClaims();
        System.out.println(map.get("phone_number"));
        System.out.println(map.get("given_name"));
        System.out.println(map.get("family_name"));
        System.out.println(map.get("email"));*/
        return orderReader.findAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable String id) {
        return orderReader.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @PostMapping("/findByCustomer")
    public Stream<Order> findByCustomer(@RequestBody PersonalInfo request) {
        return orderReader.findByCustomer(request);
    }

}