ALTER TABLE addresses
    MODIFY COLUMN telephone_number VARCHAR(255);

ALTER TABLE purchases_delivery_addresses
    MODIFY telephone_number VARCHAR(255);

ALTER TABLE purchases_billing_addresses
    MODIFY telephone_number VARCHAR(255);