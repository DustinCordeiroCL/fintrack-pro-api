-- New categories that appeared in real spending data
INSERT INTO categories (name, category_type, spending_limit, user_id)
SELECT cat.name, cat.category_type::VARCHAR, cat.spending_limit, u.id
FROM (VALUES
    ('Passeio',     'DISCRETIONARY',      0.00),
    ('Presente',    'DISCRETIONARY',      0.00),
    ('Nutri',       'DISCRETIONARY',      0.00),
    ('Assinaturas', 'DISCRETIONARY',      0.00)
) AS cat(name, category_type, spending_limit)
CROSS JOIN (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com') u;

-- ─────────────────────────────────────────
-- JANUARY 2026
-- ─────────────────────────────────────────
INSERT INTO transactions (description, amount, type, date, is_paid, category_id, user_id)
SELECT t.description, t.amount, 'EXPENSE'::VARCHAR, t.date::TIMESTAMP, true,
       (SELECT id FROM categories WHERE name = t.cat_name AND user_id = (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')),
       (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')
FROM (VALUES
    ('Aluguel',     450000.00, '2026-01-05', 'Aluguel'),
    ('Energia',      46815.00, '2026-01-13', 'Energia'),
    ('Água E',       10250.00, '2026-01-29', 'Água E'),
    ('Celular',       7390.00, '2026-01-01', 'Celular'),
    ('Comunes',      18905.00, '2026-01-05', 'Comunes'),
    ('Mercado',     336000.00, '2026-01-20', 'Mercado'),
    ('Transporte',   66000.00, '2026-01-15', 'Transporte'),
    ('Água C',        6500.00, '2026-01-18', 'Água C'),
    ('Academia',     55800.00, '2026-01-20', 'Academia'),
    ('Refeições',   114100.00, '2026-01-25', 'Refeições'),
    ('Suplementos',  28000.00, '2026-01-10', 'Suplementos'),
    ('Casa',         41500.00, '2026-01-15', 'Casa'),
    ('Bodega',       30000.00, '2026-01-22', 'Bodega'),
    ('Cosmetico',    20900.00, '2026-01-18', 'Cosmetico'),
    ('Roupas',       69144.00, '2026-01-24', 'Roupas'),
    ('Passeio',      28000.00, '2026-01-28', 'Passeio')
) AS t(description, amount, date, cat_name);

-- ─────────────────────────────────────────
-- FEBRUARY 2026
-- ─────────────────────────────────────────
INSERT INTO transactions (description, amount, type, date, is_paid, category_id, user_id)
SELECT t.description, t.amount, 'EXPENSE'::VARCHAR, t.date::TIMESTAMP, true,
       (SELECT id FROM categories WHERE name = t.cat_name AND user_id = (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')),
       (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')
FROM (VALUES
    ('Aluguel',     450000.00, '2026-02-05', 'Aluguel'),
    ('Energia',      41327.00, '2026-02-13', 'Energia'),
    ('Água E',       10250.00, '2026-02-28', 'Água E'),
    ('Celular',       6200.00, '2026-02-01', 'Celular'),
    ('Comunes',      15005.00, '2026-02-05', 'Comunes'),
    ('Mercado',     305900.00, '2026-02-20', 'Mercado'),
    ('Transporte',   20000.00, '2026-02-15', 'Transporte'),
    ('Água C',        9900.00, '2026-02-18', 'Água C'),
    ('Academia',     55800.00, '2026-02-20', 'Academia'),
    ('Refeições',   240000.00, '2026-02-25', 'Refeições'),
    ('Suplementos', 221000.00, '2026-02-10', 'Suplementos'),
    ('Casa',         21000.00, '2026-02-15', 'Casa'),
    ('Bodega',       30000.00, '2026-02-22', 'Bodega'),
    ('Cosmetico',    43000.00, '2026-02-18', 'Cosmetico'),
    ('Roupas',       11700.00, '2026-02-24', 'Roupas'),
    ('Lavanderia',   17000.00, '2026-02-25', 'Lavanderia'),
    ('Presente',     18000.00, '2026-02-14', 'Presente')
) AS t(description, amount, date, cat_name);

-- ─────────────────────────────────────────
-- MARCH 2026
-- ─────────────────────────────────────────
INSERT INTO transactions (description, amount, type, date, is_paid, category_id, user_id)
SELECT t.description, t.amount, 'EXPENSE'::VARCHAR, t.date::TIMESTAMP, true,
       (SELECT id FROM categories WHERE name = t.cat_name AND user_id = (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')),
       (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')
FROM (VALUES
    ('Aluguel',     450000.00, '2026-03-05', 'Aluguel'),
    ('Energia',      32436.00, '2026-03-13', 'Energia'),
    ('Água E',       10290.00, '2026-03-29', 'Água E'),
    ('Celular',       5000.00, '2026-03-01', 'Celular'),
    ('Comunes',      11513.00, '2026-03-05', 'Comunes'),
    ('Mercado',     315260.00, '2026-03-20', 'Mercado'),
    ('Saúde',        11000.00, '2026-03-15', 'Saúde'),
    ('Transporte',   50000.00, '2026-03-15', 'Transporte'),
    ('Água C',        8250.00, '2026-03-18', 'Água C'),
    ('Academia',     55800.00, '2026-03-20', 'Academia'),
    ('Refeições',   152150.00, '2026-03-25', 'Refeições'),
    ('Suplementos',  83990.00, '2026-03-10', 'Suplementos'),
    ('Casa',         85250.00, '2026-03-15', 'Casa'),
    ('Bodega',       30000.00, '2026-03-22', 'Bodega'),
    ('Cosmetico',     7000.00, '2026-03-18', 'Cosmetico'),
    ('Roupas',       25000.00, '2026-03-24', 'Roupas'),
    ('Lavanderia',   17000.00, '2026-03-25', 'Lavanderia'),
    ('Nutri',        33300.00, '2026-03-12', 'Nutri'),
    ('Assinaturas',  21800.00, '2026-03-01', 'Assinaturas')
) AS t(description, amount, date, cat_name);

-- ─────────────────────────────────────────
-- APRIL 2026 (partial month)
-- ─────────────────────────────────────────
INSERT INTO transactions (description, amount, type, date, is_paid, category_id, user_id)
SELECT t.description, t.amount, 'EXPENSE'::VARCHAR, t.date::TIMESTAMP, true,
       (SELECT id FROM categories WHERE name = t.cat_name AND user_id = (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')),
       (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com')
FROM (VALUES
    ('Água E',       10370.00, '2026-04-01', 'Água E'),
    ('Celular',       6510.00, '2026-04-01', 'Celular'),
    ('Mercado',     153300.00, '2026-04-10', 'Mercado'),
    ('Saúde',         3680.00, '2026-04-08', 'Saúde'),
    ('Transporte',   55800.00, '2026-04-10', 'Transporte'),
    ('Água C',        4950.00, '2026-04-05', 'Água C'),
    ('Refeições',    66580.00, '2026-04-10', 'Refeições'),
    ('Suplementos',  32990.00, '2026-04-08', 'Suplementos'),
    ('Lavanderia',    8500.00, '2026-04-10', 'Lavanderia')
) AS t(description, amount, date, cat_name);
