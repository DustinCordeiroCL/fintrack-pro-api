CREATE TABLE categories (
    id             BIGSERIAL    PRIMARY KEY,
    name           VARCHAR(255) NOT NULL UNIQUE,
    color          VARCHAR(255),
    category_type  VARCHAR(50)  NOT NULL,
    description    VARCHAR(255),
    spending_limit NUMERIC(19, 2)
);
