CREATE TABLE IF NOT EXISTS recipes.user_by_id (
    user_id varchar(150) not null,
    username varchar(100) not null,
    email varchar(150) not null,
    password varchar(150) not null,
    timestamp_created TIMESTAMP not null,
    timestamp_updated timestamp,
    UNIQUE (user_id, username, email),
    PRIMARY KEY (user_id)
);