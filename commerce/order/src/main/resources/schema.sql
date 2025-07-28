DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    cart_id UUID NOT NULL,
    payment_id UUID NOT NULL,
    delivery_id UUID NOT NULL,
    state VARCHAR(20) NOT NULL,
    weight NUMERIC(11, 3) NOT NULL,
    volume NUMERIC(11, 3) NOT NULL,
    fragile BOOLEAN NOT NULL,
    total_price NUMERIC(11, 3) NOT NULL,
    delivery_price NUMERIC(11, 3) NOT NULL,
    product_price NUMERIC(11, 3) NOT NULL
);