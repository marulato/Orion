
CREATE TABLE SYS_CONFIG(
  CONFIG_KEY VARCHAR(64) NOT NULL UNIQUE ,
  CONFIG_VALUE VARCHAR(64) NOT NULL,
  DESCRIPTION VARCHAR(256)
);

CREATE TABLE SYS_CONFIG_HX
(
  AUDIT_ID     BIGINT PRIMARY KEY AUTO_INCREMENT,
  AUDIT_TYPE   CHAR(2)     NOT NULL,
  AUDIT_TIME   DATETIME    NOT NULL,
  CONFIG_KEY   VARCHAR(64) NOT NULL,
  CONFIG_VALUE VARCHAR(64) NOT NULL,
  DESCRIPTION  VARCHAR(256),
  INDEX CONFIG_KEY_IDX(CONFIG_KEY)
);

CREATE TABLE USER_TBL
(
  USER_ID                BIGINT PRIMARY KEY AUTO_INCREMENT,
  LOGIN_ID               VARCHAR(64) UNIQUE,
  USER_DOMAIN            VARCHAR(12)  NOT NULL,
  DISPLAY_NAME           VARCHAR(128) NOT NULL,
  PWD                    VARCHAR(128) NOT NULL,
  EMAIL                  VARCHAR(128),
  MOBILE_NO              VARCHAR(15),
  ACCT_STATUS            CHAR(1)      NOT NULL,
  REMARKS                VARCHAR(64),
  LOGIN_LAST_ATTEMPT_DT  DATETIME,
  LOGIN_LAST_SUCCESS_DT  DATETIME,
  LOGIN_FAIL_ATTEMPT_CNT INT,
  PWD_LAST_CHG_DT        DATETIME,
  PWD_CHG_REQUIRED       CHAR(1),
  ACCT_ACTIVATE_DT       DATE,
  ACCT_DEACTIVATE_DT     DATE,
  CREATED_AT             DATETIME,
  CREATED_BY             VARCHAR(15),
  UPDATED_AT             DATETIME,
  UPDATED_BY             VARCHAR(15),
  INDEX MOBILE_NO_IDX (MOBILE_NO)
);
ALTER TABLE USER_TBL AUTO_INCREMENT = 10000;

DROP TABLE IF EXISTS USER_PWD_HISTORY;
CREATE TABLE USER_PWD_HISTORY(
  USER_ID                BIGINT NOT NULL,
  LOGIN_ID    VARCHAR(64) NOT NULL,
  PWD VARCHAR(128) NOT NULL,
  PWD_CHG_DT DATETIME,
  PWD_CHG_REASON  VARCHAR(32),
  INDEX USER_ID_PWD_IDX(USER_ID),
  INDEX LOGIN_ID_PWD_IDX(LOGIN_ID)
);

DROP TABLE IF EXISTS USER_TBL_HX;
CREATE TABLE USER_TBL_HX
(
  AUDIT_ID               BIGINT PRIMARY KEY AUTO_INCREMENT,
  AUDIT_TYPE             CHAR(2)  NOT NULL,
  AUDIT_TIME             DATETIME NOT NULL,
  USER_ID                BIGINT,
  LOGIN_ID               VARCHAR(64),
  USER_DOMAIN            VARCHAR(12),
  DISPLAY_NAME           VARCHAR(128),
  PWD                    VARCHAR(128),
  EMAIL                  VARCHAR(128),
  MOBILE_NO              VARCHAR(15),
  ACCT_STATUS            CHAR(1),
  REMARKS                VARCHAR(64),
  LOGIN_LAST_ATTEMPT_DT  DATETIME,
  LOGIN_LAST_SUCCESS_DT  DATETIME,
  LOGIN_FAIL_ATTEMPT_CNT INT,
  PWD_LAST_CHG_DT        DATETIME,
  PWD_CHG_REQUIRED       CHAR(1),
  ACCT_ACTIVATE_DT       DATE,
  ACCT_DEACTIVATE_DT     DATE,
  CREATED_AT             DATETIME,
  CREATED_BY             VARCHAR(15),
  UPDATED_AT             DATETIME,
  UPDATED_BY             VARCHAR(15),
  INDEX USER_ID_HXIDX(USER_ID),
  INDEX LOGIN_ID_HXIDX(LOGIN_ID)
);
ALTER TABLE USER_TBL_HX AUTO_INCREMENT = 10000;

CREATE TABLE USER_LOGIN_HISTORY
(
  USER_ID    BIGINT      NOT NULL,
  LOGIN_ID   VARCHAR(64),
  LOGIN_TIME DATETIME    NOT NULL,
  IS_SUCCESS CHAR(1)     NOT NULL,
  CLIENT     VARCHAR(64),
  SESSION_ID VARCHAR(64),
  REASON     VARCHAR(64),
  INDEX LOGIN_ID_IDX (LOGIN_ID)
);

CREATE TABLE USER_PROFILE
(
  USER_ID      BIGINT       NOT NULL,
  LOGIN_ID     VARCHAR(64)  NOT NULL,
  DISPLAY_NAME VARCHAR(128) NOT NULL,
  FULL_NAME    VARCHAR(64),
  ID_NO        VARCHAR(14),
  GENDER       CHAR(1),
  DOB          DATE,
  EMAIL        VARCHAR(128),
  CONTACT_NO   VARCHAR(18),
  DESCRIPTION  VARCHAR(128),
  PORTRAIT     MEDIUMBLOB,
  CREATED_AT   DATETIME,
  CREATED_BY   VARCHAR(15),
  UPDATED_AT   DATETIME,
  UPDATED_BY   VARCHAR(15)
);
ALTER TABLE USER_PROFILE ADD PRIMARY KEY (USER_ID, LOGIN_ID);

CREATE TABLE ERROR_CODE_MC
(
  ERROR_CODE VARCHAR(3) PRIMARY KEY,
  ERROR_DESC VARCHAR(256),
  ERROR_TYPE VARCHAR(1),
  ERROR_COND VARCHAR(255),
  CREATED_AT DATE,
  CREATED_BY VARCHAR(15),
  UPDATED_AT DATE,
  UPDATED_BY VARCHAR(15)
);

CREATE TABLE MODUEL_URL_ASSIGN
(
  MODUEL_ID   VARCHAR(64)  NOT NULL,
  MODULE_NAME VARCHAR(128) NOT NULL,
  MODUEL_DESC VARCHAR(128),
  FUNC_ID     VARCHAR(64)  NOT NULL,
  FUNC_NAME   VARCHAR(128),
  LEVEL       VARCHAR(2),
  URL         VARCHAR(512) NOT NULL,
  IS_SYSTEM   CHAR(1)      NOT NULL,
  REMARKS     VARCHAR(128)
);
ALTER TABLE MODUEL_URL_ASSIGN ADD PRIMARY KEY (MODUEL_ID, FUNC_ID);

CREATE TABLE ROLE_MODUEL_ASSIGN
(
  ROLE_ID    VARCHAR(15) NOT NULL,
  MODUEL_ID  VARCHAR(64) NOT NULL,
  FUNC_ID    VARCHAR(64) NOT NULL
);
ALTER TABLE ROLE_MODUEL_ASSIGN ADD PRIMARY KEY (ROLE_ID, FUNC_ID);

CREATE TABLE USER_ROLE_ASSIGN
(
  USER_ID BIGINT NOT NULL,
  ROLE_ID VARCHAR(15) NOT NULL,
  REMARKS VARCHAR(128)
);
ALTER TABLE USER_ROLE_ASSIGN ADD PRIMARY KEY (USER_ID, ROLE_ID);

CREATE TABLE BATCH_JOB_SCHEDUEL
(
  JOB_NAME      VARCHAR(12) PRIMARY KEY ,
  DESCRIPTION   VARCHAR(256),
  CLASS_NAME    VARCHAR(256) NOT NULL,
  IS_QUARTZ     CHAR(1)      NOT NULL,
  IS_REGISTERED CHAR(1)      NOT NULL,
  CREATED_AT    DATETIME,
  CREATED_BY    VARCHAR(15),
  UPDATED_AT    DATETIME,
  UPDATED_BY    VARCHAR(15)
);

CREATE TABLE BATCH_JOB_HISTORY
(
  JOB_ID         VARCHAR(12) NOT NULL,
  FILE_NAME      VARCHAR(64),
  INTERFACE_FILE MEDIUMBLOB,
  RUN_AT         DATETIME    NOT NULL,
  RUN_BY         VARCHAR(64) NOT NULL,
  TIME_COST      LONG        NOT NULL,
  STATUS         VARCHAR(3)  NOT NULL,
  INDEX JOB_ID_IDX (JOB_ID)

);

CREATE TABLE MASTER_CODE_MC
(
  MC_ID       INT PRIMARY KEY AUTO_INCREMENT,
  CODE_TYPE   VARCHAR(3) NOT NULL,
  CODE        VARCHAR(5) NOT NULL,
  DESCRIPTION VARCHAR(64),
  CREATED_AT  DATETIME,
  CREATED_BY  VARCHAR(15),
  UPDATED_AT  DATETIME,
  UPDATED_BY  VARCHAR(15)

);
ALTER TABLE MASTER_CODE_MC AUTO_INCREMENT = 100;

CREATE TABLE GF_BACKUP_GALLERY_TX
(
  PIC_ID      BIGINT PRIMARY KEY AUTO_INCREMENT,
  NAME        VARCHAR(64),
  FILE_NAME   VARCHAR(256) NOT NULL,
  CATEGORY    VARCHAR(32),
  DESCRIPTION VARCHAR(256),
  SIZE        BIGINT       NOT NULL DEFAULT 0,
  CONTENT     MEDIUMBLOB   NOT NULL,
  HASH        VARCHAR(128),
  CREATED_AT  DATETIME,
  CREATED_BY  VARCHAR(15),
  UPDATED_AT  DATETIME,
  UPDATED_BY  VARCHAR(15)
);
ALTER TABLE GF_BACKUP_GALLERY_TX AUTO_INCREMENT = 1000;