--后台管理系统部分（科室管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000006000', 'home', null, null, '版本管理', null, 1, null, '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000006100', 'home', null, null, 'APP版本管理', 'xj_home_000000000000000000006000', null, '/appVersion', '0');
--更新home的权限
DELETE FROM pb_re_auth_role_auth_permission where auth_role_id='00000000000000000000000000000001';
INSERT INTO pb_re_auth_role_auth_permission(id,auth_role_id, auth_permission_id) SELECT REPLACE(UUID(),'-',''),'00000000000000000000000000000001', pb_auth_permission.id FROM pb_auth_permission;
