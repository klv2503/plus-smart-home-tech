DROP TABLE IF EXISTS reserved;
DROP TABLE IF EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse (
    product_id UUID PRIMARY KEY,
    is_fragile BOOLEAN NOT NULL,
    quantity INTEGER NOT NULL,
    width NUMERIC(8, 3) NOT NULL,
    height NUMERIC(8, 3) NOT NULL,
    depth NUMERIC(8, 3) NOT NULL,
    weight DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS reserved (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    delivery_id UUID,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (order_id, product_id)
);
