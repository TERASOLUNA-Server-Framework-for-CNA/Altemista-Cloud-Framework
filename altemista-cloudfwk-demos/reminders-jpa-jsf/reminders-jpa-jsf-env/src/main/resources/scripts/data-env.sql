
-- RMNDRS_USERS

insert into RMNDRS_USERS (USERNAME, PASSWORD) values ('Admin', '1234');
insert into RMNDRS_USERS (USERNAME, PASSWORD) values ('Mario', 'password');
insert into RMNDRS_USERS (USERNAME, PASSWORD) values ('app', '1234');

-- RMNDRS_ROLES

insert into RMNDRS_ROLES (ID, ROLE) values (1, 'ROLE_USER');

-- RMNDRS_TABLE

insert into RMNDRS_TABLE (ID, USERNAME,	TEXT, DUE_DATE, COMPLETED) values (1, 'Admin', 'Build an App', '2016-06-08', false);
insert into RMNDRS_TABLE (ID, USERNAME,	TEXT, DUE_DATE, COMPLETED) values (2, 'Admin', 'Clone dinosaurs', '2016-12-31', false);
insert into RMNDRS_TABLE (ID, USERNAME,	TEXT, DUE_DATE, COMPLETED) values (3, 'Mario', 'Save Princess Peach... again', '2016-07-31', false);
insert into RMNDRS_TABLE (ID, USERNAME,	TEXT, DUE_DATE, COMPLETED) values (4, 'Admin', 'Repair the Batmobile', '2016-06-01', true);
insert into RMNDRS_TABLE (ID, USERNAME,	TEXT, DUE_DATE, COMPLETED) values (5, 'Admin', 'Destroy the One Ring in Mordor', '2016-08-01', false);
insert into RMNDRS_TABLE (ID, USERNAME,	TEXT, DUE_DATE, COMPLETED) values (6, 'Mario', 'Call Luigi', '2016-06-01', true);

--

insert into RMNDRS_USER_ROLE (ID, USERNAME, ROLE_ID) values (1, 'Admin', 1);
insert into RMNDRS_USER_ROLE (ID, USERNAME, ROLE_ID) values (2, 'Mario', 1);
insert into RMNDRS_USER_ROLE (ID, USERNAME, ROLE_ID) values (3, 'app', 1);