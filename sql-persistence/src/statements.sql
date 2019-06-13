create table inventory.parts
(
    NAME        text       not null,
    PART_NUMBER text       not null,
    BRAND       text       not null,
    CATEGORY    text       not null,
    LOCATION    text       not null,
    TEAM        VARCHAR(5) not null,
    KEYWORDS    text[],
    QUANTITY    integer    not null,
    URL         text,
    IMAGE_URL   text,
    UUID        CHAR(36) PRIMARY KEY
);

create type add_del_type as enum ('add', 'delete');

create table inventory.addition_deletion
(
    TYPE  add_del_type not null,
    UUID  CHAR(36)     not null references inventory.parts (UUID),
    TIME  TIMESTAMP    not null,
    ADDER TEXT         not null references inventory.editors (EMAIL)
);

create table inventory.consumption
(
    UUID     CHAR(36)  not null references inventory.parts (UUID),
    CONSUMED integer   not null,
    TIME     TIMESTAMP not null,
    CONSUMER TEXT      not null references inventory.editors (EMAIL)
);

create table inventory.editors
(
    ID          NUMERIC not null,
    EMAIL       TEXT primary key,
    NAME        TEXT    not null,
    PICTURE_URL TEXT    not null
);

create table inventory.whitelist
(
    EMAIL       text primary key,
    ADDER_EMAIL text      not null references inventory.editors (EMAIL),
    ADD_TIME    timestamp not null
);