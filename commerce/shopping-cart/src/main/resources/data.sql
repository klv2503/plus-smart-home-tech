INSERT INTO shopping_cart (cart_id, username, state) VALUES
('9c7e2e6d-11f6-42c8-9c93-3e46a32cbf01', 'alice', 'ACTIVE'),
('b4d2c2de-ffac-4dc0-bb7f-d9e04e5b99e5', 'bob', 'ACTIVE'),
('b1f9d7a0-2f9b-4b2c-9a55-7cce0c85f4d2', 'john', 'ACTIVE'),
('aad2e2c0-1a4d-4c58-a8a6-ecff5c181abe', 'alice', 'DEACTIVATED');

INSERT INTO shopping_cart_item (shopping_cart_id, product_code, quantity) VALUES
-- Корзина 1 (alice - ACTIVE)
('9c7e2e6d-11f6-42c8-9c93-3e46a32cbf01', '3e8d7f3c-11b2-4d9f-85c4-0b0f7c23bdfa', 4),
('9c7e2e6d-11f6-42c8-9c93-3e46a32cbf01', 'c1e53aef-9b9d-44ee-9dc0-5dcf5e8f8d3a', 8),

-- Корзина 2 (bob - ACTIVE)
('b4d2c2de-ffac-4dc0-bb7f-d9e04e5b99e5', '993f1491-0701-47c6-91f3-fb9b61cb3fc8', 12),
-- john
('b1f9d7a0-2f9b-4b2c-9a55-7cce0c85f4d2', 'f447a8f9-5df2-44a7-ae0b-88e1d01d3f3c', 5),
('b1f9d7a0-2f9b-4b2c-9a55-7cce0c85f4d2', '993f1491-0701-47c6-91f3-fb9b61cb3fc8', 7),
-- Корзина 3 (alice - DEACTIVATED)
('aad2e2c0-1a4d-4c58-a8a6-ecff5c181abe', 'f447a8f9-5df2-44a7-ae0b-88e1d01d3f3c', 7),
('aad2e2c0-1a4d-4c58-a8a6-ecff5c181abe', '1b337b3d-4a64-43df-9df5-00ed4d0ccf41', 22);