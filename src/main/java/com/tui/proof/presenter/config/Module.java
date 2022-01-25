package com.tui.proof.presenter.config;

import com.tui.proof.core.domain.service.OrderService;
import com.tui.proof.core.gateway.CustomerGateway;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import com.tui.proof.core.usecases.customer.FindAllCustomerUseCase;
import com.tui.proof.core.usecases.customer.FindByIdCustomerUseCase;
import com.tui.proof.core.usecases.customer.FindCustomerByExample;
import com.tui.proof.core.usecases.order.CreateOrderUseCase;
import com.tui.proof.core.usecases.order.FindAllOrderUseCase;
import com.tui.proof.core.usecases.order.FindByIdOrderUseCase;
import com.tui.proof.core.usecases.order.FindOrderByCustomerUseCase;
import com.tui.proof.core.usecases.order.UpdateOrderUseCase;
import com.tui.proof.data.db.mapper.CustomerMapper;
import com.tui.proof.data.db.mapper.OrderMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Module {
    @Bean
    public CustomerMapper getCustomerMapper() {
        return new CustomerMapper();
    }

    @Bean
    public OrderMapper getOrderMapper(CustomerMapper customerMapper) {
        return new OrderMapper(customerMapper);
    }
    /*
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {

            @Override
            public void customize(
                    Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(Locale.ITALIAN);
                jacksonObjectMapperBuilder
                        .serializerByType(Money.class, new JsonSerializer<Money>() {
                            @Override
                            public void serialize(
                                    Money value, JsonGenerator gen, SerializerProvider serializers)
                                    throws IOException {
                                System.out.println(value);
                                gen.writeString(format.format(value));
                            }
                        });
            }
        };
    }*/

    @Bean
    public OrderService createOrderService(TimerGateway timerGateway) {
        return new OrderService(1.33, timerGateway);
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway repository, OrderService orderService) {
        return new CreateOrderUseCase(repository, orderService);
    }

    @Bean
    public FindAllOrderUseCase findAllOrderUseCase(OrderGateway repository, OrderService orderService) {
        return new FindAllOrderUseCase(repository, orderService);
    }

    @Bean
    public FindByIdOrderUseCase findByIdOrderUseCase(OrderGateway repository, OrderService orderService) {
        return new FindByIdOrderUseCase(repository, orderService);
    }

    @Bean
    public FindOrderByCustomerUseCase findOrderByCustomerUseCase(OrderGateway repository, OrderService orderService) {
        return new FindOrderByCustomerUseCase(repository, orderService);
    }

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(OrderGateway repository, OrderService orderService) {
        return new UpdateOrderUseCase(repository, orderService);
    }

    @Bean
    public FindByIdCustomerUseCase findByIdCustomerUseCase(CustomerGateway gateway) {
        return new FindByIdCustomerUseCase(gateway);
    }

    @Bean
    public FindAllCustomerUseCase findAllCustomerUseCase(CustomerGateway gateway) {
        return new FindAllCustomerUseCase(gateway);
    }

    @Bean
    public FindCustomerByExample findCustomerByExample(CustomerGateway gateway) {
        return new FindCustomerByExample(gateway);
    }
}
