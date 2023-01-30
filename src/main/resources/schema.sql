CREATE TABLE users
(
    users_id BIGSERIAL
        CONSTRAINT users_pk
            PRIMARY KEY,
    name     VARCHAR,
    email    VARCHAR NOT NULL
        UNIQUE
);

ALTER TABLE users
    OWNER TO postgres;

CREATE TABLE requests
(
    requests_id  BIGSERIAL
        CONSTRAINT requests_pk
            PRIMARY KEY,
    description  VARCHAR,
    requestor_id BIGINT
        CONSTRAINT requests_users_fk
            REFERENCES users
);

ALTER TABLE requests
    OWNER TO postgres;

CREATE TABLE items
(
    items_id     BIGSERIAL
        CONSTRAINT items_pk
            PRIMARY KEY,
    name         VARCHAR,
    description  VARCHAR,
    is_available BOOLEAN,
    owner_id     BIGINT
        CONSTRAINT items_users_fk
            REFERENCES users,
    request_id   BIGINT
        CONSTRAINT items_requests_fk
            REFERENCES requests
);

ALTER TABLE items
    OWNER TO postgres;

CREATE TABLE bookings
(
    bookings_id BIGINT NOT NULL
        PRIMARY KEY,
    start_date  TIMESTAMP,
    end_date    TIMESTAMP,
    booker_id   BIGINT NOT NULL
        CONSTRAINT bookings_users_fk
            REFERENCES users,
    item_id     BIGINT NOT NULL
        CONSTRAINT bookings_items_fk
            REFERENCES items,
    status      VARCHAR
);

ALTER TABLE bookings
    OWNER TO postgres;

CREATE TABLE comments
(
    comments_id BIGSERIAL
        CONSTRAINT comments_pk
            PRIMARY KEY,
    text        VARCHAR,
    item_id     BIGINT
        CONSTRAINT comments_items_fk
            REFERENCES items,
    author_id   BIGINT
        CONSTRAINT comments_users_fk
            REFERENCES users
);

ALTER TABLE comments
    OWNER TO postgres;

