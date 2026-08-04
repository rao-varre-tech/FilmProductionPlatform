CREATE TABLE users
(
    id UUID PRIMARY KEY,

    email VARCHAR(150) NOT NULL UNIQUE,

    username VARCHAR(50) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100) NOT NULL,

    status VARCHAR(20) NOT NULL,

    enabled BOOLEAN NOT NULL,

    email_verified BOOLEAN NOT NULL,

    account_non_locked BOOLEAN NOT NULL,

    last_login_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_by VARCHAR(100)
);