## Summary

TUI DX Backend technical Test v2

The base project uses lombok, so you have to install it. You can use the following guide https://www.baeldung.com/lombok-ide

### Customer API
POST 	/api/v1/orders        : Create a new order
PUT 	/api/v1/orders/{id}   : Update the order information identified by "id"
DELETE	/api/v1/orders/{id}   : Delete order by "id"

### Admin API
GET 	/api/v1/admin/orders                    : Get all orders
GET 	/api/v1/admin/orders/{id}               : Get the order information identified by "id"
POST 	/api/v1/admin/orders/findByCustomer     : Search orders by customer data
GET 	/api/v1/admin/customers                 : Search all customer
GET 	/api/v1/admin/customers/{id}            : Get the customer information identified by "id"
POST 	/api/v1/admin/customers/findByExample   : Get the customer by example