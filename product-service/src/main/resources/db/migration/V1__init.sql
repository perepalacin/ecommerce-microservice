CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    mechanism VARCHAR(50),
    diameter INT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    description TEXT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_brand (brand),
    INDEX idx_mechanism (mechanism),
    INDEX idx_diameter (diameter),
    INDEX idx_price (price),
    FULLTEXT INDEX ft_name_description (name, description)
);