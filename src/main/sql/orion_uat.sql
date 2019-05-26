-- DDL generated by DBeaver
-- WARNING: It may differ from actual native database DDL

-- Drop table

-- DROP TABLE SOP6.USER_TBL;

CREATE TABLE USER_TBL
(
  USER_DOMAIN               VARCHAR(12)  NOT NULL,
  USER_ID                   VARCHAR(64) PRIMARY KEY,
  DISPLAY_NAME              VARCHAR(128) NOT NULL,
  PWD                       VARCHAR(128) NOT NULL,
  EMAIL                     VARCHAR(128),
  MOBILE_NO                 VARCHAR(64),
  ACCT_STATUS               CHAR(1)      NOT NULL,
  REMARKS                   VARCHAR(1024),
  SALUTATION                VARCHAR(12),
  FIRST_NAME                VARCHAR(128),
  MIDDLE_NAME               VARCHAR(128),
  LAST_NAME                 VARCHAR(128),
  CITIZENSHIP               VARCHAR(128),
  IDENTITY_NO               VARCHAR(128),
  PASSPORT_NO               VARCHAR(128),
  GENDER                    CHAR(1),
  DOB                       DATE,
  MARITAL_STATUS            VARCHAR(12),
  CONTACT_NO                VARCHAR(64),
  ADDR                      VARCHAR(255),
  ADDR_POSTAL_CODE          VARCHAR(12),
  ADDR_STATE                VARCHAR(128),
  ADDR_COUNTRY_CODE         CHAR(2),
  ACCT_CDT                  DATETIME,
  ACCT_UDT                  DATETIME,
  LOGIN_LAST_ATTEMPT_DT     DATETIME,
  LOGIN_LAST_SUCCESS_DT     DATETIME,
  LOGIN_2ND_LAST_SUCCESS_DT DATETIME,
  LOGIN_FAIL_ATTEMPT_CNT    INT,
  PWD_LAST_CHG_DT           DATETIME,
  PWD_CHG_REQUIRED          CHAR(1),
  PWD_CHALLENGE_QN          VARCHAR(255),
  PWD_CHALLENGE_ANS         VARCHAR(255),
  PWD_CHALLENGE_HINT        VARCHAR(255),
  ACCT_ACTIVATE_DT          DATETIME,
  ACCT_DEACTIVATE_DT        DATETIME,
  ACCT_TERMINATE_DT         DATETIME,
  IS_SYSTEM                 CHAR(1),
  IS_FIRST_TIME_LOGIN       CHAR(1),
  CREATED_AT                DATETIME,
  CREATED_BY_USERDOMAIN     VARCHAR(12),
  CREATED_BY_USERID         VARCHAR(64),
  REASON                    VARCHAR(255),
  UPDATED_AT                DATETIME,
  UPDATED_BY_USERDOMAIN     VARCHAR(12),
  UPDATED_BY_USERID         VARCHAR(64)
);

CREATE TABLE USER_LOGIN_HISTORY
(
  USER_ID    VARCHAR(64),
  LOGIN_TIME DATETIME NOT NULL,
  CLIENT     VARCHAR(64),
  SESSION_ID VARCHAR(64)

);

-- DDL generated by DBeaver
-- WARNING: It may differ from actual native database DDL

-- Drop table

-- DROP TABLE MCPSAPP.T_GF_MCPS_ERROR_MC;

CREATE TABLE ERROR_CODE_MC
(
  ERROR_CODE VARCHAR(3) PRIMARY KEY ,
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
  MODUEL_DESC VARCHAR(128),
  FUNC_DESC   VARCHAR(128),
  LEVEL       VARCHAR(2),
  URL         VARCHAR(512) NOT NULL,
  REMARKS VARCHAR(128)
);

CREATE TABLE ROLE_MODUEL_ASSIGN
(
  ROLE_ID   VARCHAR(15) NOT NULL,
  MODUEL_ID VARCHAR(64) NOT NULL
);

CREATE TABLE USER_ROLE_ASSIGN(
  USER_ID VARCHAR(64) NOT NULL,
  ROLE_ID VARCHAR(15) NOT NULL,
  REMARKS VARCHAR(128)
);
