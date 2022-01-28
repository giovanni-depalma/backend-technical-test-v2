## Summary

TUI DX Backend technical Test v2

The base project uses lombok, so you have to install it. You can use the following guide https://www.baeldung.com/lombok-ide

### Swaggers
/api/v1/swagger-ui/index.html

### oauth
http://localhost:10000/auth/realms/tui-pilotes/.well-known/openid-configuration


### Customer API
POST 	/api/public/orders        : Create a new order
PUT 	/api/public/orders/{id}   : Update the order information identified by "id"

### Admin API
GET 	/api/admin/orders                    : Get all orders
GET 	/api/admin/orders/{id}               : Get the order information identified by "id"
POST 	/api/admin/orders/findByCustomer     : Search orders by customer data
GET 	/api/admin/customers                 : Search all customer
GET 	/api/admin/customers/{id}            : Get the customer information identified by "id"
POST 	/api/admin/customers/findByExample   : Get the customer by example



/opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.realmName=tui-pilotes \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/realm-export.json