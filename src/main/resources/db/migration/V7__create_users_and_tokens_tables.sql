CREATE TABLE users (
                       id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email      VARCHAR(255) NOT NULL UNIQUE,
                       password   VARCHAR(255) NOT NULL,
                       role       VARCHAR(20)  NOT NULL DEFAULT 'USER',
                       enabled    BOOLEAN      NOT NULL DEFAULT true,
                       created_at TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE refresh_tokens (
                                id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                token      VARCHAR(500) NOT NULL UNIQUE,
                                user_id    UUID NOT NULL REFERENCES users(id),
                                expired    BOOLEAN NOT NULL DEFAULT false,
                                revoked    BOOLEAN NOT NULL DEFAULT false,
                                created_at TIMESTAMP NOT NULL DEFAULT now(),
                                expires_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

-- usuario admin por defecto (password: admin123)
INSERT INTO users (email, password, role)
VALUES ('admin@marketplace.com',
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
        'ADMIN');