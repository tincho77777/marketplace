CREATE TABLE outbox (
                        id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        event_type    VARCHAR(100)  NOT NULL,
                        payload       TEXT          NOT NULL,
                        status        VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
                        created_at    TIMESTAMP     NOT NULL DEFAULT now(),
                        processed_at  TIMESTAMP
);

CREATE INDEX idx_outbox_status ON outbox(status);