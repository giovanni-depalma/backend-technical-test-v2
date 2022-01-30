package com.tui.proof.presenter.rest;

import com.tui.proof.util.FakeListBuilder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@WebMvcTest(AdminCustomerController.class)
@WithMockUser(username="admin",roles={"ADMIN"})
public class AdminCustomerControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerReader customerReader;

    public static ResultMatcher[] checkCustomers(List<Customer> customers) {
        return IntStream.range(0, customers.size()).mapToObj(i -> checkCustomer("$["+i+"].", customers.get(i))).flatMap(Arrays::stream).toArray(ResultMatcher[]::new);
    }

    public static ResultMatcher[] checkCustomer(Customer customer) {
        return checkCustomer("", customer);
    }

    public static ResultMatcher[] checkCustomer(String prefix, Customer customer) {
        return new ResultMatcher[]{
                jsonPath(prefix+"personalInfo.firstName").value(is(customer.getPersonalInfo().getFirstName())),
                jsonPath(prefix+"personalInfo.lastName").value(is(customer.getPersonalInfo().getLastName())),
                jsonPath(prefix+"personalInfo.email").value(is(customer.getPersonalInfo().getEmail())),
                jsonPath(prefix+"personalInfo.telephone").value(is(customer.getPersonalInfo().getTelephone())),
                jsonPath(prefix+"id").value(is(customer.getId()), Long.class)
        };
    }

    @Test
    public void shouldFindAllCustomers() throws Exception {
        int expectedSize = 2;
        List<Customer> expected = FakeListBuilder.buildList(expectedSize, FakeCustomer::buildCustomer);
        when(customerReader.findAll()).thenReturn(expected.stream());
        this.mockMvc.perform(get("/admin/customers")).andExpect(status().isOk())
                .andExpectAll(checkCustomers(expected));
    }

    @Test
    public void shouldFindCustomersByExample() throws Exception {
        int expectedSize = 2;
        PersonalInfo request = FakeCustomer.buildPersonalInfo();
        List<Customer> expected = FakeListBuilder.buildList(expectedSize, FakeCustomer::buildCustomer);
        when(customerReader.findByExample(request)).thenReturn(expected.stream());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/admin/customers/findByExample").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isOk())
                .andExpectAll(checkCustomers(expected));
    }


    @Test
    public void shouldFindCustomer() throws Exception {
        long id = 1;
        Customer expected = FakeCustomer.buildCustomer();
        when(customerReader.findById(id)).thenReturn(Optional.of(expected));
        this.mockMvc.perform(get("/admin/customers/"+id)).andExpect(status().isOk())
                .andExpectAll(checkCustomer(expected));
    }

    @Test
    public void shouldNotFindCustomer() throws Exception {
        long id = 1;
        when(customerReader.findById(id)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/admin/customers/"+id)).andExpect(status().isNotFound());
    }
*/
}
