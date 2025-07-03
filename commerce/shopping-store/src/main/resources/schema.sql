DROP TABLE IF EXISTS products;

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    image_src VARCHAR(100),
    quantity_state VARCHAR(15) NOT NULL,
    product_state VARCHAR(15) NOT NULL,
    product_category VARCHAR(15) NOT NULL,
    price NUMERIC(8,2) NOT NULL
);
