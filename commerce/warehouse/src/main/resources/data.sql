INSERT INTO warehouse (product_id, is_fragile, quantity, width, height, depth, weight) VALUES
('3e8d7f3c-11b2-4d9f-85c4-0b0f7c23bdfa', true, 35, 1.200, 0.700, 0.5200, 1.25),
('c1e53aef-9b9d-44ee-9dc0-5dcf5e8f8d3a', false, 25, 2.000, 1.500, 1.000, 3.80),
('f447a8f9-5df2-44a7-ae0b-88e1d01d3f3c', true, 30, 0.710, 0.450, 0.330, 0.90),
('1b337b3d-4a64-43df-9df5-00ed4d0ccf41', false, 100, 0.500, 0.300, 0.400, 2.20),
('993f1491-0701-47c6-91f3-fb9b61cb3fc8', false, 60, 1.100, 0.900, 0.800, 4.55);

INSERT INTO reserved (order_id, product_id, delivery_id, quantity) VALUES
('d6624e66-9382-480c-a93c-e7f026657064', '3e8d7f3c-11b2-4d9f-85c4-0b0f7c23bdfa',
'de36850e-d847-4472-99a3-04b9fd9c4a74', 4),
('d6624e66-9382-480c-a93c-e7f026657064', 'c1e53aef-9b9d-44ee-9dc0-5dcf5e8f8d3a',
'de36850e-d847-4472-99a3-04b9fd9c4a74', 8);