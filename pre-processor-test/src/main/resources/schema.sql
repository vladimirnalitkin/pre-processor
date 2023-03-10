drop table IF EXISTS ADDRESS;
create TABLE ADDRESS(
  ID bigint NOT NULL AUTO_INCREMENT,
  HOUSE_NUMBER VARCHAR(255),
  STREET VARCHAR(255) NOT NULL,
  ZIP_CODE VARCHAR(255),
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);

drop table IF EXISTS HOUSE;
create TABLE HOUSE(
  ID bigint NOT NULL AUTO_INCREMENT,
  PRBR_ID bigint NOT NULL,
  NUM bigint NOT NULL,
  ADDRESS_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);

drop table IF EXISTS COMPANY;
create TABLE COMPANY(
  ID bigint NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(255),
  TEST_NAME VARCHAR(255),
  ZIP bigint,
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE KEY COMPANY_NAME (NAME)
);

drop table IF EXISTS DEPARTMENT;
create TABLE DEPARTMENT(
  ID bigint NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(255),
  COMPANY_ID BIGINT,
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);

drop table IF EXISTS EMPLOYEE;
create TABLE EMPLOYEE(
  ID bigint NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(255),
  SURNAME VARCHAR(255),
  ADDRESS_ID BIGINT,
  DEPARTMENT_ID BIGINT,
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);

drop table IF EXISTS ARTICLE;
create TABLE ARTICLE(
  ID bigint NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(255),
  TITLE VARCHAR(255),
  CONTENT VARCHAR(2550),
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);

drop table IF EXISTS ARTICLE_COMMENT;
create TABLE ARTICLE_COMMENT(
  ID bigint NOT NULL AUTO_INCREMENT,
  ARTICLE_ID bigint NOT NULL,
  POSTED_BY VARCHAR(255),
  CONTENT VARCHAR(2550),
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);