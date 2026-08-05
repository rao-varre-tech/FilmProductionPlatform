CREATE TABLE email_verification_tokens
(
    id UUID PRIMARY KEY,

    token VARCHAR(255) NOT NULL UNIQUE,

    user_id UUID NOT NULL,

    expires_at TIMESTAMP NOT NULL,
     
    verified BOOLEAN NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_by VARCHAR(100),

    CONSTRAINT fk_email_verification_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_email_verification_tokens_token
ON email_verification_tokens(token);

CREATE INDEX idx_email_verification_tokens_user
ON email_verification_tokens(user_id);