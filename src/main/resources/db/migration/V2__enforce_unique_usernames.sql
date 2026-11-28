ALTER TABLE users
    MODIFY username VARCHAR(255) NOT NULL,
    ADD CONSTRAINT uk_users_username UNIQUE (username),
    ADD CONSTRAINT chk_users_username_nonblank
        CHECK (CHAR_LENGTH(TRIM(username)) > 0);
