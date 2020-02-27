
-- DROP tables if exists
DROP TABLE T_RECIPE_INGREDIENT IF EXISTS;
DROP TABLE T_RECIPE IF EXISTS;
DROP TABLE T_INGREDIENT IF EXISTS;
DROP TABLE T_UNIT IF EXISTS;
drop table DEMO_TABLE if exists;

--
-- Table structure for table T_UNIT
--
CREATE TABLE T_UNIT (
	ID INTEGER IDENTITY NOT NULL PRIMARY KEY,
	NAME VARCHAR(250) NOT NULL,
	GRAMS INTEGER NOT NULL
)

--
-- Table structure for table T_INGREDIENT
--

CREATE TABLE T_INGREDIENT (
	ID INTEGER IDENTITY NOT NULL PRIMARY KEY,
	NAME VARCHAR(250) NOT NULL,
);

--
-- Table structure for table T_RECIPE
--

CREATE TABLE T_RECIPE (
	ID INTEGER IDENTITY NOT NULL PRIMARY KEY,
	NAME VARCHAR(250) NOT NULL,
	BAKING_TEMP INTEGER NOT NULL,
	BAKING_TIME INTEGER NOT NULL
);

--
-- Table structure for table T_RECIPE_INGREDIENT
--

CREATE TABLE T_RECIPE_INGREDIENT (
	ID INTEGER IDENTITY NOT NULL PRIMARY KEY,
	RECIPE_ID INTEGER NOT NULL,
	INGREDIENT_ID INTEGER NOT NULL,
	AMOUNT INTEGER NOT NULL,
	UNIT_ID INTEGER NOT NULL
);


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