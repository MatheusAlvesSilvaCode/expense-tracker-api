-- Expense Tracker API - init (PostgreSQL)
-- Idempotente: pode correr mais de uma vez sem rebentar.

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,          -- nunca guardes a password em texto
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS categories (
    id   SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO categories (name) VALUES
    ('Groceries'),
    ('Leisure'),
    ('Electronics'),
    ('Utilities'),
    ('Clothing'),
    ('Health'),
    ('Others')
ON CONFLICT (name) DO NOTHING;

CREATE TABLE IF NOT EXISTS expenses (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id  SMALLINT      NOT NULL REFERENCES categories(id),
    description  VARCHAR(255)  NOT NULL,
    amount       NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    expense_date DATE          NOT NULL DEFAULT CURRENT_DATE,
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- Cobre os filtros (semana, mês, 3 meses, intervalo custom) por utilizador
CREATE INDEX IF NOT EXISTS idx_expenses_user_date
    ON expenses (user_id, expense_date DESC);
