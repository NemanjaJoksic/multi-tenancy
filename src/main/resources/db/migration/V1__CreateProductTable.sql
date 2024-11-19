CREATE SEQUENCE IF NOT EXISTS product_id_seq AS INTEGER;

CREATE TABLE
    IF NOT EXISTS product (
        id BIGINT,
        name TEXT NOT NULL,
        count INTEGER NOT NULL,
        CONSTRAINT productPK PRIMARY KEY (id)
    );
