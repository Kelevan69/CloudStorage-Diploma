-- Таблица пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(128) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       locked BOOLEAN NOT NULL DEFAULT FALSE,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       credentials_expired BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_login TIMESTAMP WITH TIME ZONE,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS 'Таблица для хранения данных пользователей';
COMMENT ON COLUMN users.email IS 'Уникальный email пользователя';
COMMENT ON COLUMN users.password_hash IS 'Хеш пароля (BCrypt)';
COMMENT ON COLUMN users.role IS 'Роль пользователя (USER, ADMIN)';

-- Таблица файлов
CREATE TABLE files (
                       id BIGSERIAL PRIMARY KEY,
                       filename VARCHAR(255) NOT NULL,
                       content BYTEA NOT NULL,
                       size BIGINT NOT NULL CHECK (size >= 0),
                       user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT unique_filename_per_user UNIQUE (user_id, filename)
);

COMMENT ON TABLE files IS 'Таблица для хранения метаданных и содержимого файлов';
COMMENT ON COLUMN files.filename IS 'Имя файла (уникальное в рамках пользователя)';
COMMENT ON COLUMN files.content IS 'Бинарное содержимое файла';
COMMENT ON COLUMN files.size IS 'Размер файла в байтах';

-- Индексы
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_files_created_at ON files (created_at DESC);
CREATE INDEX idx_files_user ON files (user_id);

-- Функция для обновления временной метки обновления
CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

-- Триггеры для автоматического обновления временной метки
CREATE TRIGGER update_users_modified
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_modified_column();

CREATE TRIGGER update_files_modified
    BEFORE UPDATE ON files
    FOR EACH ROW EXECUTE FUNCTION update_modified_column();
