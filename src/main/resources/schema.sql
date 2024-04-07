DROP TABLE IF EXISTS books;
CREATE TABLE books
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    author             VARCHAR(255)          NOT NULL,
    isbn               varchar(255) UNIQUE NULL,
    price              float8                NOT NULL,
    title              VARCHAR(255)          NOT NULL,
    created_date       timestamp             NOT NULL,
    last_modified_date timestamp             NOT NULL,
    version            integer               NOT NULL
);