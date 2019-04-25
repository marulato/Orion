CREATE TABLE USER_ACCOUNT_TBL(

  ACCT_ID       BIGINT PRIMARY KEY AUTO_INCREMENT,
  USER_ID       VARCHAR (25) NOT NULL UNIQUE,
  DOMAIN        VARCHAR (15) NOT NULL,
  DISPLAY_NAME  VARCHAR (25) NOT NULL,
  PWD           VARCHAR (128) NOT NULL,
  STATUS        VARCHAR (2) NOT NULL,
  EMAIL         VARCHAR (64) NOT NULL,
  LAST_LOGIN    DATETIME,
  SEC_LOGIN     DATETIME,
  CREATED_AT 		DATETIME NOT NULL,
	CREATED_BY 		VARCHAR(45) NOT NULL,
	UPDATED_AT 		DATETIME NOT NULL,
	UPDATED_BY 		VARCHAR(45) NOT NULL

);
ALTER TABLE USER_ACCOUNT_TBL AUTO_INCREMENT=1000000000;

CREATE TABLE USER_AUDIT_HX(

  AUDIT_ID      BIGINT PRIMARY KEY AUTO_INCREMENT,
  USER_ID       VARCHAR (25) NOT NULL,
  AUDIT_TYPE    VARCHAR (2) NOT NULL,
  AUDIT_TIME    DATETIME NOT NULL,
  IP            VARCHAR (16),
  CREATED_AT 		DATETIME NOT NULL,
	CREATED_BY 		VARCHAR(45) NOT NULL,
	UPDATED_AT 		DATETIME NOT NULL,
	UPDATED_BY 		VARCHAR(45) NOT NULL,

	INDEX USER_ID_AUDIT_IDX(USER_ID)
);

CREATE TABLE USER_PROFILE_TX(

  USER_ID       VARCHAR (25) NOT NULL PRIMARY KEY,
  DISPLAY_NAME  VARCHAR (25) NOT NULL,
  REAL_NAME     VARCHAR (25),
  GENDER        VARCHAR (1) NOT NULL,
  AGE           VARCHAR (3),
  BIRTHDAY      DATE,
  EMAIL         VARCHAR (64) NOT NULL,
  CONTACT       VARCHAR (15),
  PORTRAIT      MEDIUMBLOB,
  LOCATION      VARCHAR (64),
  COLLEGE       VARCHAR (64),
  CORPORATION   VARCHAR (128),
  TRADE         VARCHAR (25),
  CONSTELLATION VARCHAR (3),
  QQ            VARCHAR (15),
  WECHAT        VARCHAR (15),
  CREATED_AT 		DATETIME NOT NULL,
	CREATED_BY 		VARCHAR(45) NOT NULL,
	UPDATED_AT 		DATETIME NOT NULL,
	UPDATED_BY 		VARCHAR(45) NOT NULL
);

CREATE TABLE USER_ROLE_ASSIGN_TX(

  ASSIGN_ID     BIGINT PRIMARY KEY AUTO_INCREMENT,
  USER_ID       VARCHAR (25) NOT NULL,
  ROLE_ID       VARCHAR (10) NOT NULL,
  ROLE_NAME     VARCHAR (25) NOT NULL,
  CREATED_AT 		DATETIME NOT NULL,
	CREATED_BY 		VARCHAR(45) NOT NULL,
	UPDATED_AT 		DATETIME NOT NULL,
	UPDATED_BY 		VARCHAR(45) NOT NULL,

	INDEX USER_ID_ROLE_IDX(USER_ID)

);
ALTER TABLE USER_ROLE_ASSIGN_TX AUTO_INCREMENT=1000000000;
ALTER TABLE USER_ROLE_ASSIGN_TX ADD FOREIGN KEY USER_ID_FK(USER_ID) REFERENCES USER_ACCOUNT_TBL(USER_ID);

CREATE TABLE ROLE_TBL(

  ROLE_ID       VARCHAR (10) NOT NULL PRIMARY KEY,
  ROLE_NAME     VARCHAR (25) NOT NULL,
  LEVEL         VARCHAR (10) NOT NULL,
  STATUS        VARCHAR (1) NOT NULL,
  DESCRIPTION   VARCHAR (256) NOT NULL,
  CREATED_AT 		DATETIME NOT NULL,
	CREATED_BY 		VARCHAR(45) NOT NULL,
	UPDATED_AT 		DATETIME NOT NULL,
	UPDATED_BY 		VARCHAR(45) NOT NULL
);

CREATE TABLE ROLE_AUTHORITY_TBL(

  ROLE_AUTH_ID  BIGINT PRIMARY KEY AUTO_INCREMENT,
  ROLE_ID       VARCHAR (10) NOT NULL,
  ROLE_NAME     VARCHAR (25) NOT NULL,
  COMPONENT     VARCHAR (64) NOT NULL,
  PART          VARCHAR (64) NOT NULL,
  URI           VARCHAR (512) NOT NULL,

  INDEX ROLE_ID_AUTH_IDX(ROLE_ID)
);
ALTER TABLE ROLE_AUTHORITY_TBL AUTO_INCREMENT=10000;

CREATE TABLE BATCH_JOB_INTERFACE_TX(

  BATCH_ID        BIGINT PRIMARY KEY AUTO_INCREMENT,
  BATCHJOB_ID     VARCHAR (15) NOT NULL,
  BATCHJOB_NAME   VARCHAR (128) NOT NULL,
  STATUS          VARCHAR (25) NOT NULL,
  INPUT_FILE      MEDIUMBLOB,
  OUTPUT_FILE     MEDIUMBLOB,
  START_TIME      DATETIME NOT NULL,
  END_TIME        DATETIME NOT NULL,
  EMAIL_CONTENT   BLOB,
  CREATED_AT 		  DATETIME NOT NULL,
	CREATED_BY 		  VARCHAR(45) NOT NULL,
	UPDATED_AT 		  DATETIME NOT NULL,
	UPDATED_BY 		  VARCHAR(45) NOT NULL,

	INDEX BATCHJOB_ID_IDX(BATCHJOB_ID)
);
ALTER TABLE BATCH_JOB_INTERFACE_TX AUTO_INCREMENT=10000;

CREATE TABLE ERROR_CODE_MC(
  ERROR_CODE    VARCHAR (3) PRIMARY KEY,
  ERROR_TITLE   VARCHAR (64) NOT NULL,
  DESCRIPTION   VARCHAR (256),
  ERROR_TYPE    VARCHAR (3) NOT NULL,
  CREATED_AT 		DATETIME NOT NULL,
	CREATED_BY 		VARCHAR(45) NOT NULL,
	UPDATED_AT 		DATETIME NOT NULL,
	UPDATED_BY 		VARCHAR(45) NOT NULL
);

CREATE TABLE EA_ENTITLEMENT_TX(
  ENTITLEMENT_ID        BIGINT PRIMARY KEY AUTO_INCREMENT,
  PRINCIPLE_ID          BIGINT,
  COMMAND               VARCHAR (3) NOT NULL,
  SS_IDNO               VARCHAR(10),
  IDCARD_NO             VARCHAR(18) NOT NULL,
  DATE_JOINED_SS        DATE,
  SS_TYPE               VARCHAR (2),
  NAME                  VARCHAR (24) NOT NULL,
  SEX                   VARCHAR (1) NOT NULL,
  DATE_OF_BIRTH         DATE NOT NULL,
  CENSUS_REGISTER       VARCHAR (4) NOT NULL,
  SS_LOCATION           VARCHAR (12) NOT NULL,
  RETIRED_DATE          DATE,
  CREATED_AT 		        DATETIME NOT NULL,
	CREATED_BY 		        VARCHAR(45) NOT NULL,
	UPDATED_AT 		        DATETIME NOT NULL,
	UPDATED_BY 		        VARCHAR(45) NOT NULL,

	INDEX SSID_IDX(SS_IDNO),
	INDEX IDCARD_IDX(IDCARD_NO)

);
ALTER TABLE EA_ENTITLEMENT_TX AUTO_INCREMENT=10000;
ALTER TABLE EA_ENTITLEMENT_TX ADD CONSTRAINT PRINCIPLE_ID_FK FOREIGN KEY (PRINCIPLE_ID) REFERENCES EA_PRINCIPLE_TX(PRINCIPLE_ID);

CREATE TABLE EA_PRINCIPLE_TX(
  PRINCIPLE_ID          BIGINT PRIMARY KEY AUTO_INCREMENT,
);
ALTER TABLE EA_PRINCIPLE_TX AUTO_INCREMENT=10000;