CREATE TABLE users (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID(), TRUE)) NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

CREATE TABLE addresses (
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
    default_address BOOLEAN,
    user_id BINARY(16) NOT NULL,
    CONSTRAINT fk_user_in_addresses FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);