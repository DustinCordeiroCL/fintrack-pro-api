-- Demo user (password: #Dustin88)
INSERT INTO users (name, email, password, created_at)
VALUES ('Dustin Cordeiro',
        'cordeiro.dustin00@gmail.com',
        '$2a$10$yocWVaUTZD5OsYp9x2PypemnVAR2/KNlXYLyY.X/ZdT34vYuSvLAG',
        NOW());

-- Essential categories
INSERT INTO categories (name, category_type, spending_limit, user_id)
SELECT cat.name, cat.category_type::VARCHAR, cat.spending_limit, u.id
FROM (VALUES
    ('Aluguel',    'ESSENTIAL', 450000.00),
    ('Energia',    'ESSENTIAL',  40000.00),
    ('Água E',     'ESSENTIAL',  12000.00),
    ('Celular',    'ESSENTIAL',  10000.00),
    ('Comunes',    'ESSENTIAL',  20000.00),
    ('Mercado',    'ESSENTIAL', 300000.00),
    ('Saúde',      'ESSENTIAL',      0.00),
    ('Transporte', 'ESSENTIAL',  20000.00),
    ('Água C',     'ESSENTIAL',  30500.00)
) AS cat(name, category_type, spending_limit)
CROSS JOIN (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com') u;

-- Discretionary categories
INSERT INTO categories (name, category_type, spending_limit, user_id)
SELECT cat.name, cat.category_type::VARCHAR, cat.spending_limit, u.id
FROM (VALUES
    ('Academia',   'DISCRETIONARY',  55800.00),
    ('Refeições',  'DISCRETIONARY', 100000.00),
    ('Suplementos','DISCRETIONARY', 100000.00),
    ('Casa',       'DISCRETIONARY', 100000.00),
    ('Bodega',     'DISCRETIONARY',  30000.00),
    ('Cosmetico',  'DISCRETIONARY',  20000.00),
    ('Roupas',     'DISCRETIONARY',  60000.00),
    ('Lavanderia', 'DISCRETIONARY',  17000.00)
) AS cat(name, category_type, spending_limit)
CROSS JOIN (SELECT id FROM users WHERE email = 'cordeiro.dustin00@gmail.com') u;
