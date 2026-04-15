CREATE TABLE refresh_tokens (
    id          BIGSERIAL    PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     BIGINT       NOT NULL REFERENCES users (id),
    expiry_date TIMESTAMP    NOT NULL
);
