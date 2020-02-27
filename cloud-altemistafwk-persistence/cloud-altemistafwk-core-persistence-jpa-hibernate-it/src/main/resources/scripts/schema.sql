
-- DROP tables if exists
drop table T_RECIPE_INGREDIENT if exists;
drop table T_RECIPE if exists;
drop table T_INGREDIENT if exists;
drop table T_UNIT if exists;

--
-- Table structure for table T_UNIT
--
create table T_UNIT (
	ID integer identity not null primary key,
	NAME varchar(250) not null,
	GRAMS integer not null
)

--
-- Table structure for table T_INGREDIENT
--

create table T_INGREDIENT (
	ID integer identity not null primary key,
	NAME varchar(250) not null,
);

--
-- Table structure for table T_RECIPE
--

create table T_RECIPE (
	ID integer identity not null primary key,
	NAME varchar(250) not null,
	BAKING_TEMP integer not null,
	BAKING_TIME integer not null
);

--
-- Table structure for table T_RECIPE_INGREDIENT
--

create table T_RECIPE_INGREDIENT (
	ID integer identity not null primary key,
	RECIPE_ID integer not null,
	INGREDIENT_ID integer not null,
	AMOUNT integer not null,
	UNIT_ID integer not null
);
