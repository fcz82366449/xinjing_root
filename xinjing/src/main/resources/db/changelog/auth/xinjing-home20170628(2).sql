--后台管理系统部分（医生管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000011000000000002400', 'home', null, null, '与患者视频权限', 'xj_home_000000000000000000002000', 2, '/calllimit', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000022000000000002400', 'home', null, null, '医生咨询收费记录', 'xj_home_000000000000000000002000', 2, '/charges', '0');
--更新home的权限
DELETE FROM pb_re_auth_role_auth_permission where auth_role_id='00000000000000000000000000000001';
INSERT INTO pb_re_auth_role_auth_permission(id,auth_role_id, auth_permission_id) SELECT REPLACE(UUID(),'-',''),'00000000000000000000000000000001', pb_auth_permission.id FROM pb_auth_permission;
