package com.tui.proof.presenter.rest;

import lombok.extern.log4j.Log4j2;

import com.tui.proof.old.OrdersConfiguration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class FooController {

  private final OrdersConfiguration configuration;

  public FooController(OrdersConfiguration configuration) {
    this.configuration = configuration;
  }


  @GetMapping("/")
  void test() {
    log.info(configuration);
    log.info("Foo controller");
  }
}
