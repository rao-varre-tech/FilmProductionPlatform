CREATE TABLE audit_logs (

    id UUID PRIMARY KEY,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    created_by VARCHAR(255),

    updated_by VARCHAR(255),

    action VARCHAR(100) NOT NULL,

    status VARCHAR(50) NOT NULL,

    user_id UUID,

    username VARCHAR(100) NOT NULL,

    ip_address VARCHAR(100),

    user_agent VARCHAR(1000),

    details VARCHAR(1000),

    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE INDEX idx_audit_logs_user
ON audit_logs(user_id);

CREATE INDEX idx_audit_logs_action
ON audit_logs(action);

CREATE INDEX idx_audit_logs_created_at
ON audit_logs(created_at);

CREATE INDEX idx_audit_logs_status
ON audit_logs(status);