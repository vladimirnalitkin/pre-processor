drop table if exists ADDRESS;
CREATE TABLE ADDRESS(
  ID bigint NOT NULL AUTO_INCREMENT,
  HOUSE_NUMBER VARCHAR(255),
  STREET VARCHAR(255),
  ZIP_CODE VARCHAR(255),
  PRBR_ID bigint NOT NULL,
  COMPANY_ID bigint NOT NULL,
  PRIMARY KEY (ID)
);

drop table if exists COMPANY;
CREATE TABLE COMPANY(
  ID bigint NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(255),
  PRBR_ID bigint NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE KEY COMPANY_NAME (NAME)
);

--alter table DEPARTMENT add CONSTRAINT DEPARTMENT_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID);
--alter table EMPLOYEE add CONSTRAINT EMPLOYEE_ADDRESS FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS(ID);
--alter table EMPLOYEE add CONSTRAINT EMPLOYEE_DEPARTMENT FOREIGN KEY (DEPARTMENT_ID) REFERENCES DEPARTMENT(ID);