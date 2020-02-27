
-- DROP tables if exists
drop table RMNDRS_TABLE if exists;
drop table RMNDRS_USERS if exists;
drop table RMNDRS_ROLES if exists;

--
-- Table structure for table RMNDRS_TABLE
--
create table RMNDRS_TABLE (
	ID integer identity not null primary key,
	USERNAME varchar(64) not null,
	TEXT varchar(1024) not null,
	DUE_DATE date not null,
	COMPLETED boolean default false
);

--
-- Table structure for table RMNDRS_USERS
--
create table RMNDRS_USERS (
	USERNAME varchar(64) not null primary key,
	PASSWORD varchar(64) not null
);

--
-- Table structure for table RMNDRS_ROLES
--
CREATE TABLE RMNDRS_ROLES (
  ID integer identity not null primary key,
  ROLE varchar(45) NOT NULL,
);

CREATE TABLE RMNDRS_USER_ROLE (
  ID integer identity not null primary key,
  USERNAME varchar(64) not null,
  ROLE_ID integer not null
);
--
-- Relationships
--
alter table RMNDRS_TABLE add constraint USERNAME_FK foreign key (USERNAME) references RMNDRS_USERS (USERNAME);
alter table RMNDRS_USER_ROLE add constraint USER_ROLE_USERNAME_FK foreign key (USERNAME) references RMNDRS_USERS (USERNAME);
alter table RMNDRS_USER_ROLE add constraint USER_ROLE_ROLE_ID_FK foreign key (ROLE_ID) references RMNDRS_ROLES (ID);