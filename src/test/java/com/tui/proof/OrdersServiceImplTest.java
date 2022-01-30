package com.tui.proof;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrdersServiceImplTest {

//    @Autowired
//    private OrdersConfiguration ordersConfiguration;

//    @Autowired
//    private PurchaserOrdersService ordersService;

//    @MockBean
//    private OrdersRepository mockRepository;
/*
    @TestFactory
    public Stream<DynamicTest> testAllAllowedQuantity() {
        return ordersConfiguration.getOrderableQuantity().stream()
                .map(pilotes -> DynamicTest.dynamicTest("Test Create Order with pilots: " + pilotes, () -> {
                    Mockito.reset(mockRepository);
                    Mockito.when(mockRepository.createOrder(Mockito.any())).thenReturn(1);
                    Order order = new Order();
                    order.setPilotes(pilotes);
                    Order result = ordersService.createOrder(order);
                    Assertions.assertAll(
                        () -> assertNotNull(result.getNumber()),
                        () -> assertEquals(pilotes*ordersConfiguration.getPrice(), result.getOrderTotal())
                    );
                    verify(mockRepository, times(1)).createOrder(any());
                }));
    }
*/

}
