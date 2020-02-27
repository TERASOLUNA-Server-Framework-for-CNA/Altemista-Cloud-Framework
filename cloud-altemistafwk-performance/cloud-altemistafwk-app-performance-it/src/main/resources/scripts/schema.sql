
-- DROP tables if exists
drop table DEMO_TABLE if exists;

--
-- Table structure for table DEMO_TABLE
--
create table DEMO_TABLE (
	KEY integer identity not null primary key,
	
	INTEGER_FIELD integer,
	INT_FIELD int,
	SMALLINT_FIELD smallint,
	TINYINT_FIELD tinyint,
	BIGINT_FIELD bigint,
	DECIMAL_FIELD decimal,
	NUMERIC_FIELD numeric,
	FLOAT_FIELD float,
	DOUBLE_FIELD double,
	
	DATE_FIELD date,
	DATETIME_FIELD datetime,
	TIMESTAMP_FIELD timestamp,
	TIME_FIELD time,
	
	CHAR_FIELD char(255),
	VARCHAR_FIELD varchar(255),
	BINARY_FIELD binary(255),
	VARBINARY_FIELD varbinary(255),
	
	BLOB_FIELD blob
)
