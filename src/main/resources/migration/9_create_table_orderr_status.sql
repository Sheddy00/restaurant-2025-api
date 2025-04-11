CREATE TABLE IF NOT EXISTS orderr_status(
    id SERIAL PRIMARY KEY,
    status orders_status,
    created_at TIMESTAMP
);