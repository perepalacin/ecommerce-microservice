CREATE TABLE purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    status VARCHAR(255),
    delivery_date DATETIME(6) NOT NULL
);

CREATE TABLE purchases_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    purchase_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_purchase_items_purchases_id
        FOREIGN KEY (purchase_id)
        REFERENCES purchases(id)
        ON DELETE CASCADE
);

CREATE TABLE purchases_billing_addresses (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     full_name VARCHAR(255) NOT NULL,
     telephone_number SMALLINT NOT NULL,
     address_first_line VARCHAR(255),
     address_second_line VARCHAR(255),
     postalCode VARCHAR(255),
     city VARCHAR(255),
     province VARCHAR(255),
     country VARCHAR(255),
     vat_id VARCHAR(255),
     user_id BINARY(16) NOT NULL,
     purchase_id BIGINT NOT NULL,
     CONSTRAINT fk_billing_address_purchases_id
        FOREIGN KEY (purchase_id)
        REFERENCES purchases(id)
        ON DELETE CASCADE
);

CREATE TABLE purchases_delivery_addresses (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     full_name VARCHAR(255) NOT NULL,
     telephone_number SMALLINT NOT NULL,
     address_first_line VARCHAR(255),
     address_second_line VARCHAR(255),
     postalCode VARCHAR(255),
     city VARCHAR(255),
     province VARCHAR(255),
     country VARCHAR(255),
     vat_id VARCHAR(255),
     user_id BINARY(16) NOT NULL,
     purchase_id BIGINT NOT NULL,
     CONSTRAINT fk_delivery_address_purchases_id
        FOREIGN KEY (purchase_id)
        REFERENCES purchases(id)
        ON DELETE CASCADE
);