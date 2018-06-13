--后台管理系统部分（患者管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000001000', 'home', null, null, '患者管理', null, 1, null, '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000001100', 'home', null, null, '患者管理', 'xj_home_000000000000000000001000', null, '/userPatient', '0');
--后台管理系统部分（医生管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000002000', 'home', null, null, '医生管理', null, 2, null, '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000002100', 'home', null, null, '医生管理', 'xj_home_000000000000000000002000', 1, '/userDoctor', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000002200', 'home', null, null, '预约管理', 'xj_home_000000000000000000002000', 3, '/doctorAppointment', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000002300', 'home', null, null, '文章管理', 'xj_home_000000000000000000002000', 5, '/doctorArticle', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000002400', 'home', null, null, '电子处方', 'xj_home_000000000000000000002000', 7, '/prescription', '0');
--后台管理系统部分（内容管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000003000', 'home', null, null, '内容管理', null, 3, null, '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000003100', 'home', null, null, '病种管理', 'xj_home_000000000000000000003000', 1, '/disease', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000003200', 'home', null, null, '疗法管理', 'xj_home_000000000000000000003000', 3, '/therapy', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000003300', 'home', null, null, '内容管理', 'xj_home_000000000000000000003000', 5, '/content', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000003400', 'home', null, null, '评价管理', 'xj_home_000000000000000000003000', 7, '/comment', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000003500', 'home', null, null, '订单管理', 'xj_home_000000000000000000003000', 9, '/order', '0');
--后台管理系统部分（VR室管理）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000004000', 'home', null, null, 'VR室管理', null, 4, null, '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000004100', 'home', null, null, 'VR室管理', 'xj_home_000000000000000000004000', 1, '/vrRoom', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000004200', 'home', null, null, '预约管理', 'xj_home_000000000000000000004000', 3, '/vrRoomAppointment', '0');
--后台管理系统部分（报表统计）
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000005000', 'home', null, null, '报表统计', null, 5, null, '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000005100', 'home', null, null, '内容购买排行', 'xj_home_000000000000000000005000', 1, '/statement/contentBuy', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000005200', 'home', null, null, '用户付费排行', 'xj_home_000000000000000000005000', 3, '/statement/userPay', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000005300', 'home', null, null, '医生结算报表', 'xj_home_000000000000000000005000', 5, '/settlement/doctorSettlement', '0');
INSERT INTO `pb_auth_permission` VALUES ('xj_home_000000000000000000005400', 'home', null, null, '医院结算报表', 'xj_home_000000000000000000005000', 7, '/settlement/hospitalSettlement', '0');
--更新home的权限
DELETE FROM pb_re_auth_role_auth_permission where auth_role_id='00000000000000000000000000000001';
INSERT INTO pb_re_auth_role_auth_permission(id,auth_role_id, auth_permission_id) SELECT REPLACE(UUID(),'-',''),'00000000000000000000000000000001', pb_auth_permission.id FROM pb_auth_permission;
