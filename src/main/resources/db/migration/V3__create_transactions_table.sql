CREATE TABLE transactions (
    id          BIGSERIAL      PRIMARY KEY,
    description VARCHAR(255)   NOT NULL,
    amount      NUMERIC(19, 2) NOT NULL,
    date        TIMESTAMP      NOT NULL,
    type        VARCHAR(50)    NOT NULL,
    category_id BIGINT         NOT NULL REFERENCES categories (id),
    due_day     INTEGER,
    is_paid     BOOLEAN        NOT NULL DEFAULT FALSE
);
