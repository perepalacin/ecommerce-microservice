ALTER TABLE products
ADD COLUMN public_url VARCHAR(255) UNIQUE;

UPDATE products
SET public_url = CONCAT(
    REPLACE(LOWER(brand), ' ', '-'),
    '-',
    REPLACE(LOWER(name), ' ', '-')
);