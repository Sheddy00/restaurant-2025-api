CREATE TABLE IF NOT EXISTS dish_available (
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(100) NOT NULL,
    available_status DECIMAL(10,2)
);