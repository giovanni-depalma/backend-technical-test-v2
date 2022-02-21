CREATE TABLE customer_data
(
    id         UUID default random_uuid() PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE ,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    telephone  VARCHAR(255) NOT NULL
);

CREATE TABLE order_data
(
    id                  UUID default random_uuid() PRIMARY KEY,
    pilotes             INTEGER NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    editable_until      TIMESTAMP WITH TIME ZONE NOT NULL,
    total               NUMERIC(20, 2) NOT NULL,
    delivery_city       VARCHAR(255) NOT NULL,
    delivery_country    VARCHAR(255) NOT NULL,
    delivery_street     VARCHAR(255) NOT NULL,
    delivery_postcode   VARCHAR(255) NOT NULL,
    customer            UUID NOT NULL,
    CONSTRAINT "order_to_customer_FK" FOREIGN KEY (customer)
        REFERENCES customer_data (id) ON DELETE RESTRICT
);
