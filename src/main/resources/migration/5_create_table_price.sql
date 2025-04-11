CREATE TABLE IF NOT EXISTS price(
    id SERIAL PRIMARY KEY,
    amount NUMERIC(10,5),
    date_value date,
    id_ingredient INT REFERENCES ingredient(id)
);