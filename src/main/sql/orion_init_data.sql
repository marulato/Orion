INSERT INTO SYS_CONFIG (CONFIG_KEY, CONFIG_VALUE, DESCRIPTION)
VALUES ('orion.security.password.expiry', '365', '密码有效期'),
       ('orion.security.password.history', '3', '历史密码保留'),
       ('orion.security.password.failure', '10', '允许密码错误次数'),
       ('orion.security.password.minLength', '5', '允许最小密码长度'),
       ('orion.security.password.maxLength', '20', '允许最大密码长度'),
       ('orion.security.password.algorithm', 'AES/GCM/NoPadding', '配置的密码算法'),
       ('orion.security.password.userAlgorithm', 'SHA-BCrypto15', '账户密码算法'),
       ('orion.security.account.expiry', '3650', '账户有效期'),
       ('orion.security.account.inactive', '180', '账户休眠'),
       ('orion.settings.ftp.host', '192.168.0.101', 'FTP服务器'),
       ('orion.settings.ftp.username', 'minions', 'FTP用户名'),
       ('orion.settings.ftp.password', '', 'FTP密码'),
       ('orion.settings.ftp.port', '21', 'FTP端口');








INSERT INTO ERROR_CODE_MC (ERROR_CODE, ERROR_DESC, ERROR_TYPE, ERROR_COND, CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY)
VALUES ('000', '非法请求，页面可能过期，或请求方式不被接受', 'E', '请求被拒绝，token验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('001', '描述不能为空且长度不能超过256个字符', 'I', '系统配置提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('002', '值不能为空且长度不得超过64个字符', 'I', '系统配置提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('003', '任务名称不能为空且长度不得超过32个字符', 'I', '计划任务提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('004', '任务组不能为空且长度不得超过32个字符', 'I', '计划任务提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('005', '数据库中已存在相同计划的任务', 'I', '计划任务提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('006', '目标类不存在或不是有效的计划任务类型', 'I', '计划任务提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('007', '输入的Cron不是合法的Cron表达式', 'I', '计划任务提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP'),
       ('008', 'Cron和目标类不能同时与现有任务重复', 'I', '计划任务提交，验证失败', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP', STR_TO_DATE('2019-01-01','%Y-%m-%d'), 'SYSTEM_SETUP');