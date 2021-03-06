## Summary

TUI DX Backend technical Test v2

### Get Started Immediately

In order to start the project  follow these simple instructions:

- Run 'docker-compose build' from project folder
- Run 'docker-compose up -d' from project folder
- Wait for services initialization
- Launch Browser on page: http://localhost:8081/swagger-ui/index.html

#### Create your first order 
Use POST /orders
```json
{
  "pilotes": 5,
  "delivery": {
    "street": "Poco Mas Drive",
    "postcode": "24205",
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
You have 5 minutes, or you will get a 409 error!
If you use an invalid id you will get a 404 error!

```json
{
  "pilotes": 15,
  "delivery": {
    "street": "Poco Mas Drive Change Alternative",
    "postcode": "24206",
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
- MapStruct
- Spring Boot 2.6.2
- Spring WebFlux
- Spring Data r2dbc (with H2)
- Spring Security
- Spring Boot Actuator
- Identity Server (Keycloak)

#### Money
An application must always manage money carefully. Libraries as "Joda-Money" or "Moneta" were evaluated but considered excessive.
A Money class was therefore created, as a simple wrapper of a BigDecimal.

#### Time
The application manages timetables with the Instant class, and serialize the date in UTC 

```
2022-01-30T21:47:00.173464Z
```

#### Security
The application it's configured as "resource server" and interact with an Identity Server (Keycloak).
The docker-compose launch a preconfigured instance of Keycloak, with the "tui-pilotes" realm.
The following roles are also configured:

- admin (used by app)
- customer (actually not used)

The following users have been configured for testing
- tui-admin/tui-admin (admin)
- tui-customer/tui-customer (customer)
- tui-all/tui-all (admin,customer)

NOTE: the configuration of the identity server is development oriented, do not use it directly in production!

The approach is to deny everything except what is authorized.
It's possible configure the public endpoint via "application.yml"

```yml
spring:
  main:
    security:
      white_list:
        - /swagger-ui/**
        - /v3/api-docs/**
        - /purchaserOrders/**
        - /profile/**
```

#### Spring Boot Actuator

Actuator endpoints let you monitor and interact with your application. In particular, the following endpoints have been activated through the "application.yml" file

  - health: shows application health information.
  - prometheus: exposes metrics in a format that can be scraped by a Prometheus server

### The Project
The project it's logically divided in:

- domain
- presenter
- repositories 
- service: business logic

#### Domain
It contains the entities and the business rules (like for example the time frame in which the order can be changed)
The main entities are "Order" and "Customer", are identified by "uuid" and are composed by base objects (like Address, Money, PersonalInfo).

There are many topics about using "uuid" vs "Serial", with advantages and disadvantages.
Decision must be taken based on requirement, in this simple case both choices were acceptable.

To manage the Order-Customer relationship and avoid multiple read query the annotation "NamedEntityGraph" was used.

One of the business rules is to prevent an order from being changed after 5 minutes.

**During the creation phase, the instant until it is possible to modify the order is set**


#### Service
It's implements the business service using entities and repository.
The order management is divided into 2 services:

- OrderService: management of the order
- CustomerService: management of the customer

#### Repositories
It contains the repositories:

- CustomerRepository
- OrderRepository

And  simple converter, to manage the limits of r2dbc about relationships and nested objects. 

#### Presenter
It contains "Controller", "Serializer" and any mapping objects.


### Configuration

The configuration in "application.yml" allows to configure:

- a list of "allowed_quantity"
- price in € for a single pilotes
- time interval in seconds to change the order

```yml
spring:
    main:
    orders:
        allowed_quantity:
        - 5
        - 10
        - 15
        price: "1.33"
        closed_after_seconds: 300
```

It's also configured the security, with "oauth2 resourceserver"
If you want to start the application without security, you can use "dev-unsecured" profile.

### Validation
All data are validated, they may not cover all national standards, but they seem reasonable.

In particular: 

- All data are mandatory
- All string are max 100 characters (some are more restrictive)

**Address**

- street: max 100 characters
- postcode of 5 number: /^\d{5}$/
- city: max 100 characters
- country: max 100 characters

**Person**

- email must be valid and max 100 characters
- phone numbers have several standards according to the country, so is a simple string (max 30 characters)
- firstName: max 100 characters
- lastName: 100 characters



### Integration Test

In the folder testing-apis-with-cypress there are simple APIs integration tests with Cypress.

To install and start the tests execute the commands:

```
yarn install
yarn test
```

### Dockerfile
Dockerfile it's multistage, to achieve two purposes:

- best size for the final image
- simulate a pipeline, including a "Test Stage" 


### docker-compose

The following docker-compose have been prepared:

- docker-compose
- docker-compose-full

#### docker-compose

docker-compose starts 2 services
- identity server
- the app

To start / stop  use the following commands
```
docker-compose build
docker-compose up -d
docker-compose stop
docker-compose down
```

You can only start "identity service" during development to test security.

```
docker-compose up identity-server
```

#### docker-compose-full

The Docker plugin "grafana/loki-docker-driver" must be installed. Run the following command to install the plugin:

```
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
```

To check installed plugins, use the docker plugin ls command. Plugins that have started successfully are listed as enabled:

```
$ docker plugin ls
ID                  NAME         DESCRIPTION           ENABLED
ac720b8fcfdb        loki         Loki Logging Driver   true
```

docker-compose starts the following services:
- identity server
- the app
- prometheus: to collect metrics
- loki: to aggregate logs
- grafana: to view logs and metrics



To log in to Grafana open your web browser and go to http://localhost:3000/ . On the login page enter "tui-admin" for username and password.

3 dashboards have been configured:

- Logging Dashboard via Loki
- JVM (Micrometer)
- Order Statistics

To start / stop  use the following commands

```
docker-compose -f docker-compose-full.yml build
docker-compose -f docker-compose-full.yml up -d
docker-compose -f docker-compose-full.yml stop
docker-compose -f docker-compose-full.yml start
```


