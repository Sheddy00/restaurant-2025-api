CREATE TABLE IF NOT EXISTS stock_movement(
    id SERIAL PRIMARY KEY,
    movement_type mouvement_type NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit unit NOT NULL,
    creation_datetime TIMESTAMP NOT NULL,
    id_ingredient INT REFERENCES ingredient(id)
);