INSERT INTO permissions
(id, name, description, created_at, updated_at, created_by, updated_by)
VALUES

(gen_random_uuid(), 'USER_READ', 'View users', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'USER_CREATE', 'Create users', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'USER_UPDATE', 'Update users', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'USER_DELETE', 'Delete users', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'ROLE_READ', 'View roles', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'ROLE_CREATE', 'Create roles', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'ROLE_UPDATE', 'Update roles', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'ROLE_DELETE', 'Delete roles', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'PERMISSION_READ', 'View permissions', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'PERMISSION_ASSIGN', 'Assign permissions', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'PROJECT_CREATE', 'Create projects', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'PROJECT_UPDATE', 'Update projects', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'PROJECT_DELETE', 'Delete projects', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'CASTING_MANAGE', 'Manage casting', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'SCRIPT_CREATE', 'Create scripts', NOW(), NOW(), 'admin', 'admin'),
(gen_random_uuid(), 'SCRIPT_UPDATE', 'Update scripts', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'PAYMENT_MANAGE', 'Manage payments', NOW(), NOW(), 'admin', 'admin'),

(gen_random_uuid(), 'REPORT_VIEW', 'View reports', NOW(), NOW(), 'admin', 'admin');