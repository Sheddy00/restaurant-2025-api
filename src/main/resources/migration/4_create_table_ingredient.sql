CREATE TABLE IF NOT EXISTS ingredient (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit_price NUMERIC(10,5) NOT NULL,
    unit unit NOT NULL,
    update_datetime TIMESTAMP
);
