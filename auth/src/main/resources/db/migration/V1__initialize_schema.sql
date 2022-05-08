CREATE TABLE IF NOT EXISTS "user_role" (
  id            VARCHAR(50)     PRIMARY KEY,
  user_name     VARCHAR(100)    NOT NULL,
  password      VARCHAR(100)    NOT NULL,
  roles         VARCHAR(255)
);