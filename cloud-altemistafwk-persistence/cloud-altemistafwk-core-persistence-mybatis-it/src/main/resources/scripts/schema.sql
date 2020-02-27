
-- DROP tables if exists
drop table DEMO_TABLE if exists;

--
-- Table structure for table DEMO_TABLE
--
create table DEMO_TABLE (
	KEY integer identity not null primary key,
	TINYINT_FIELD TINYINT,
	INTEGER_FIELD integer
)
