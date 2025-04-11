CREATE TABLE IF NOT EXISTS dish_ingredient (
    id SERIAL PRIMARY KEY,
    id_dish INT REFERENCES dish(id),
    id_ingredient INT REFERENCES ingredient(id),
    required_quantity FLOAT,
    unit unit
);