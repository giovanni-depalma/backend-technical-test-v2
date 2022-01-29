package com.tui.proof.presenter.rest;

import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Test")
@BasePathAwareController
public class TestController2 {
    
    @PostMapping(value = "/test/findByCustomer2")
    public Stream<Order> findByCustomer(@RequestBody PersonalInfo request) {
        return null;
    }
}
