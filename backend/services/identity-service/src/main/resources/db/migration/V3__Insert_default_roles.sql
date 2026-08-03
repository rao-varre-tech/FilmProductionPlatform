CREATE EXTENSION IF NOT EXISTS pgcrypto;
INSERT INTO roles
(id,name,description,system,created_at,updated_at)

VALUES
(gen_random_uuid(),'ADMIN','System Administrator',true,NOW(),NOW()),
(gen_random_uuid(),'USER','Default User',true,NOW(),NOW()),
(gen_random_uuid(),'PRODUCER','Producer',true,NOW(),NOW()),
(gen_random_uuid(),'DIRECTOR','Director',true,NOW(),NOW()),
(gen_random_uuid(),'WRITER','Writer',true,NOW(),NOW()),
(gen_random_uuid(),'ARTIST','Artist',true,NOW(),NOW()),
(gen_random_uuid(),'TECHNICIAN','Technician',true,NOW(),NOW()),
(gen_random_uuid(),'CREW','Crew',true,NOW(),NOW());