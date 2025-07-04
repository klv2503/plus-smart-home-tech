DROP TABLE IF EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse (
    product_id UUID PRIMARY KEY,
    is_fragile BOOLEAN NOT NULL,
    quantity INTEGER NOT NULL,
    width NUMERIC(7, 3) NOT NULL,
    height NUMERIC(7, 3) NOT NULL,
    depth NUMERIC(7, 3) NOT NULL,
    weight DOUBLE PRECISION NOT NULL
);
