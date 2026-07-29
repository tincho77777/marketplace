CREATE TABLE idempotency_keys (
                                  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  idempotency_key VARCHAR(255) NOT NULL UNIQUE,
                                  response        TEXT NOT NULL,
                                  created_at      TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_idempotency_key ON idempotency_keys(idempotency_key);