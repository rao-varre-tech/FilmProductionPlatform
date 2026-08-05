INSERT INTO role_permissions
(id, role_id, permission_id, created_at, updated_at, created_by, updated_by)

SELECT
gen_random_uuid(),
r.id,
p.id,
NOW(),
NOW(),
'admin',
'admin'

FROM roles r
JOIN permissions p
ON p.name IN
(
'USER_READ'
)

WHERE r.name='USER';

INSERT INTO role_permissions
(id,
 role_id,
 permission_id,
 created_at,
 updated_at,
 created_by,
 updated_by)

SELECT
gen_random_uuid(),
r.id,
p.id,
NOW(),
NOW(),
'admin',
'admin'

FROM roles r
CROSS JOIN permissions p

WHERE r.name='ADMIN';