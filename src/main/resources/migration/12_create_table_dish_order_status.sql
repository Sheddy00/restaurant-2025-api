CREATE TABLE IF NOT EXISTS dish_order_status(
    id SERIAL PRIMARY KEY,
    status orders_status,
    created_at TIMESTAMP
);