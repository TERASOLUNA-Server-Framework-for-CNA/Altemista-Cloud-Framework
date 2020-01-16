--
-- Table structure for table t_account
--

drop table T_ACCOUNT if exists;
create table T_ACCOUNT (ID integer identity primary key, NUMBER varchar(9), NAME varchar(50) not null, unique(NUMBER));


--
-- Table structure for table t_account_beneficiary
--

DROP TABLE IF EXISTS T_ACCOUNT_BENEFICIARY;

CREATE TABLE T_ACCOUNT_BENEFICIARY (
  ID integer identity primary key,
  ACCOUNT_ID integer DEFAULT NULL,
  NAME varchar(50) DEFAULT NULL ,
  ALLOCATION_PERCENTAGE double NOT NULL,
  SAVINGS double NOT NULL,
  unique(NAME, ACCOUNT_ID),
  FOREIGN KEY (ACCOUNT_ID) REFERENCES T_ACCOUNT(ID)
)

--
-- Table structure for table t_account_beneficiary
--

DROP TABLE IF EXISTS T_ACCOUNT_CREDIT_CARD;

CREATE TABLE T_ACCOUNT_CREDIT_CARD (
  ID integer identity primary key,
  ACCOUNT_ID integer DEFAULT NULL,
  NUMBER varchar(16) DEFAULT NULL ,
  unique(ACCOUNT_ID),
  FOREIGN KEY (ACCOUNT_ID) REFERENCES T_ACCOUNT(ID)
)

--
-- Table structure for table t_restaurant
--

DROP TABLE IF EXISTS T_RESTAURANT;

CREATE TABLE T_RESTAURANT (
  ID integer identity primary key,
  MERCHANT_NUMBER varchar(10) DEFAULT NULL,
  NAME varchar(80) DEFAULT NULL ,
  BENEFIT_PERCENTAGE double DEFAULT NULL,
  BENEFIT_AVAILABILITY_POLICY varchar(50) DEFAULT NULL ,
  unique(MERCHANT_NUMBER)  
)

--
-- Table structure for table t_restaurant
--

DROP TABLE IF EXISTS T_REWARD;

CREATE TABLE T_REWARD (
  ID integer identity primary key,
  CONFIRMATION_NUMBER varchar(25) DEFAULT NULL,
  REWARD_AMOUNT double DEFAULT NULL,
  REWARD_DATE date DEFAULT NULL,
  ACCOUNT_NUMBER varchar(9) DEFAULT NULL,
  DINING_AMOUNT double DEFAULT NULL,
  DINING_MERCHANT_NUMBER varchar(10) DEFAULT NULL,
  DINING_DATE date DEFAULT NULL,
  unique(CONFIRMATION_NUMBER)  
)
