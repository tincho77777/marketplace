CREATE TABLE products (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description VARCHAR(500) NOT NULL,
                        price NUMERIC(15,2) NOT NULL,
                        stock INTEGER NOT NULL,
                        category VARCHAR(50) NOT NULL
                      );