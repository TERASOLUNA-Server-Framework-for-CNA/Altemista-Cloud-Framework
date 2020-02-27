
-- DROP tables if exists
drop table T_ORM_EXAMPLE if exists;

--
-- Table structure for table T_ORM_EXAMPLE
--
create table T_ORM_EXAMPLE (
	NAME varchar(16) not null primary key,
	DESCRIPTION varchar(256)
);
