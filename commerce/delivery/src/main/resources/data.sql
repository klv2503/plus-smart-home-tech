-- Вставка адресов
INSERT INTO addresses (country, city, street, house, flat)
VALUES
    ('ADDRESS_1', 'ADDRESS_1', 'ADDRESS_1', 'ADDRESS_1', 'ADDRESS_1'),
    ('Germany', 'Hamburg', 'Hafenstraße', '7B', '21');

-- Вставка доставки
INSERT INTO delivery (delivery_id, from_address, to_address, order_id, state)
VALUES (
    'de36850e-d847-4472-99a3-04b9fd9c4a74',
    (SELECT id FROM addresses WHERE country = 'ADDRESS_1' AND city = 'ADDRESS_1' AND street = 'ADDRESS_1' AND house = 'ADDRESS_1' AND flat = 'ADDRESS_1'),
    (SELECT id FROM addresses WHERE country = 'Germany' AND city = 'Hamburg' AND street = 'Hafenstraße' AND house = '7B' AND flat = '21'),
    'd6624e66-9382-480c-a93c-e7f026657064',
    'CREATED'
);