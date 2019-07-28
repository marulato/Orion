INSERT INTO SYS_CONFIG (CONFIG_KEY, CONFIG_VALUE, DESCRIPTION)
VALUES('orion.security.password.expiry', '365', '密码有效期'),
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