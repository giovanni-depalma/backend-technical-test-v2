## Summary

TUI DX Backend technical Test v2

### Get Started Immediately

In order to start the project  follow these simple instructions:

- Run 'docker-compose build' from project folder
- Run 'docker-compose up -d' from project folder
- Wait for services initialization
- Launch Browser on page: http://localhost:8081/swagger-ui/index.html

#### Create your first order 
Use POST /purchaserOrders
```json
{
  "pilotes": 5,
  "delivery": {
    "street": "Poco Mas Drive",
    "postcode": "2420 ",
    "city": "Frisco",
    "country": "TX"
  },
  "customer": {
    "email": "EufrasioAlmanzaCepeda@armyspy.com",
    "firstName": "Eufrasio Almanza",
    "lastName": "Cepeda",
    "telephone": "214-474-0425"
  }
}
```
track the order id for next updates

#### Update your order
Retrieve the saved id and update your order with PUT /orders/{id}.
You have 5 minutes or you will get a 409 error!
If you use an invalid id you will get a 404 error!

```json
{
  "pilotes": 15,
  "delivery": {
    "street": "Poco Mas Drive Change Alternative",
    "postcode": "2420 Alt",
    "city": "Frisco Change Alt",
    "country": "TX Change Alt"
  },
  "customer": {
    "email": "EufrasioAlmanzaCepeda@armyspy.com",
    "firstName": "Eufrasio Almanza Change Alternative",
    "lastName": "Cepeda Change Alternative",
    "telephone": "214-474-0425 213"
  }
}
```

#### View the orders
To see the orders you have to use "/orders" endpoint. You can choose between:
- GET /orders get all orders, with sort and pagination  (not required by testing)
- GET /orders/{id} get the order by id (not required by testing)
- POST /orders/findByCustomer: Allow partial searches: e.g., all orders of customers whose
  name contains an “a” in their name (mandatory for testing)

These endpoints are dedicated to "Miquel Montoro" and the operations are "secured".
From Swagger UI clicking on the padlock you have two options:

- insert bearer token: you can get the token with the following command
```
curl -X POST "http://localhost:8080/auth/realms/tui-pilotes/protocol/openid-connect/token" \
 --insecure \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=tui-admin" \
 -d "password=tui-admin" \
 -d 'grant_type=password' \
 -d 'client_secret=8JNumTbP0pj8iYoCNgrycb3srA4jBHly' \
 -d "client_id=tui-gateway" | jq -r '.access_token'
```
- insert username/password (tui-admin/tui-admin)

Now you can make requests (The token has a 15-minute expiration)

These are examples of partial search

```json
{
"email": ".com",
"firstName": "Eufrasio",
"lastName": "hange Alternative",
"telephone": "214"
}
```

```json
{
"email": "",
"firstName": "Eufrasio"
}
```
and the borderline case
```json
{

}
```

Have fun!

### Quick Overview

The project uses the following stack:

- JDK 17
- Lombok
- Spring Boot 2.6.2
- Spring Web
- Spring Data JPA
- Spring Data Rest
- Spring Security
- Keycloak as Identity Server

The API (in my interpretation) are dedicated to two main actors:

- Miquel Montoro using the secure api
- The customer using the public api

Spring Data Rest allowed to expose a good API for the administrator, reducing the boilerplate code.
This will allow for easier maintenance (and evolution) of the code.
On the other hand, it reduces the layers by directly exposing the model but this was found to be acceptable, especially for "secured" API.

To these bees are added those of 2 controllers:
    - AdminOrderController: manages the search for customer
    - PurchaserOrderController: manages the creation and updating of an order

### Money
An application must always manage money carefully. Libraries as "Joda-Money" or "Moneta" were evaluated but considered excessive.
A Money class was therefore created, as a simple wrapper of a BigDecimal.

### Time
The application manages timetables with the Instant class, and serialize the date in UTC 

```
2022-01-30T21:47:00.173464Z
```

### Security
The application it's configured as "resource server" and interact with an Identity Server (Keycloak).
The docker-compose launch a preconfigured instance of Keycloak, with the "tui-pilotes" realm.
The following roles are also configured:

- admin (used by app)
- customer (actually not used)

The following users have been configured for testing
- tui-admin/tui-admin (admin)
- tui-customer/tui-customer (customer)
- tui-all/tui-all (admin,customer)






```json
{
  "firstName": "John",
  "lastName": "Smith",
  "age": 25
}
```

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