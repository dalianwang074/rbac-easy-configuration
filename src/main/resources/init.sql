ALTER TABLE permission ADD UNIQUE (annotion_id);

-- 创建用户
BEGIN;
INSERT INTO `user` VALUES ('1', 'E10ADC3949BA59ABBE56E057F20F883E', 'ADMIN');
COMMIT;

-- 创建角色
BEGIN;
INSERT INTO `role` VALUES ('1', 'ADMIN'), ('2', 'GUEST');
COMMIT;

