CREATE TABLE IF NOT EXISTS users (login          VARCHAR(64) NOT NULL PRIMARY KEY,password       VARCHAR(64) NOT NULL,user_name      VARCHAR(64) NOT NULL);


--CREATE TABLE IF NOT EXISTS recipes
--(
--    id          UUID NOT NULL PRIMARY KEY,
--    name        TEXT NOT NULL,
--    image_url   TEXT,
--    ingridients TEXT[] NOT NULL,
--    steps       TEXT[] NOT NULL,
--    video_url   TEXT,
--    tags
--    user_name     BIGINT NOT NULL,
--)
