create table if not exists users
(
    users_id bigserial
        constraint users_pk
            primary key,
    name     varchar,
    email    varchar not null UNIQUE
);

alter table users
    owner to postgres;

create table if not exists requests
(
    requests_id  bigserial
        constraint requests_pk
            primary key,
    description   varchar,
    requestor_id bigint
        constraint requests_users_fk
            references users
);

alter table requests
    owner to postgres;

create table if not exists items
(
    items_id     bigserial
        constraint items_pk
            primary key,
    name         varchar,
    description  varchar,
    is_available boolean,
    owner_id     bigint
        constraint items_users_fk
            references users,
    request_id   bigint
        constraint items_requests_fk
            references requests
);

alter table items
    owner to postgres;

create table if not exists bookings
(
    bookings_id bigint not null
        primary key,
    start_date  timestamp,
    end_date    timestamp,
    booker_id   bigint not null
        constraint bookings_users_fk
            references users,
    item_id     bigint not null
        constraint bookings_items_fk
            references items,
    status      varchar
);

alter table bookings
    owner to postgres;

create table if not exists comments
(
    comments_id bigserial
        constraint comments_pk
            primary key,
    text        varchar,
    item_id     bigint
        constraint comments_items_fk
            references items,
    author_id   bigint
        constraint comments_users_fk
            references users
);

alter table comments
    owner to postgres;

