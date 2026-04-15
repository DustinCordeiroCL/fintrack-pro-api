-- Add user ownership to categories (issue #15 — data isolation)
ALTER TABLE categories
    ADD COLUMN user_id BIGINT NOT NULL REFERENCES users (id);

ALTER TABLE categories
    DROP CONSTRAINT IF EXISTS categories_name_key;

ALTER TABLE categories
    ADD CONSTRAINT uk_category_name_user UNIQUE (name, user_id);

-- Add user ownership to transactions (issue #15 — data isolation)
ALTER TABLE transactions
    ADD COLUMN user_id BIGINT NOT NULL REFERENCES users (id);
