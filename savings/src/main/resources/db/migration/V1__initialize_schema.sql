CREATE SCHEMA IF NOT EXISTS savings;
SET search_path TO savings, public;

CREATE TABLE account
(
    id         VARCHAR(36) PRIMARY KEY,
    name       VARCHAR(64)    NOT NULL,
    type       VARCHAR(64)    NOT NULL,
    balance    NUMERIC(15, 6) NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP      NOT NULL
);

INSERT INTO account
values ('972b950a-f9e1-47f1-ace3-98612021f44a', 'Amal', 'SAVINGS', 100, now(), now());
INSERT INTO account
values ('c166f572-8379-4811-92f4-210c0d2c5a81', 'Raj', 'SAVINGS', 100, now(), now());
INSERT INTO account
values ('ade4562e-80a6-43fa-835a-b6788f68a7f8', 'Vinoth', 'SAVINGS', 100, now(), now());
INSERT INTO account
values ('23987317-27f8-485a-ac18-2847b09a992d', 'James', 'SAVINGS', 100, now(), now());
INSERT INTO account
values ('2e95f318-9ed4-4905-b73f-ace882b8e953', 'John', 'SALARY', 100, now(), now());