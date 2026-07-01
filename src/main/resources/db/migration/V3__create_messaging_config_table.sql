CREATE TABLE messaging_config (
                                  id          SERIAL PRIMARY KEY,
                                  provider    VARCHAR(20) NOT NULL,  -- 'RABBIT' o 'SQS'
                                  active      BOOLEAN NOT NULL DEFAULT true,
                                  updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

INSERT INTO messaging_config (provider) VALUES ('RABBIT');