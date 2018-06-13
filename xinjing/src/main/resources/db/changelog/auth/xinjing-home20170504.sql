--后台管理系统部分（VR室管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000004400', 'home', null, null, '医院管理', 'xj_home_000000000000000000004000', 2, '/hospital', '0');
--更新home的权限
DELETE FROM pb_re_auth_role_auth_permission where auth_role_id='00000000000000000000000000000001';
INSERT INTO pb_re_auth_role_auth_permission(id,auth_role_id, auth_permission_id) SELECT REPLACE(UUID(),'-',''),'00000000000000000000000000000001', pb_auth_permission.id FROM pb_auth_permission;
