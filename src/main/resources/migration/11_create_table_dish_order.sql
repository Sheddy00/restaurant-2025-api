CREATE TABLE IF NOT EXISTS dish_order (
    id SERIAL PRIMARY KEY,
    id_order INT REFERENCES orders(id),
    id_dish INT REFERENCES dish(id),
    quantity_to_order DECIMAL(10,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);