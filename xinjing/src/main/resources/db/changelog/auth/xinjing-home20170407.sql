--前台管理系统部分（医生审核管理）
INSERT INTO pb_auth_permission VALUES ('xj_home_000000000000000000002150', 'home', null, null, '医生审核管理', 'xj_home_000000000000000000002000', 2, '/userDoctorApprove', '0');

--更新ADMIN的权限
DELETE FROM pb_re_auth_role_auth_permission where auth_role_id='00000000000000000000000000000001';
INSERT INTO pb_re_auth_role_auth_permission(id,auth_role_id, auth_permission_id) SELECT REPLACE(UUID(),'-',''),'00000000000000000000000000000001', pb_auth_permission.id FROM pb_auth_permission;

