ALTER TABLE purchases_delivery_addresses
ADD COLUMN purchase_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);