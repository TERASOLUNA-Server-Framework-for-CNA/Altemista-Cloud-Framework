
-- T_UNIT values
insert into T_UNIT (ID, NAME, GRAMS) values (1, 'gram', 1);
insert into T_UNIT (ID, NAME, GRAMS) values (2, 'kilogram', 1000);
insert into T_UNIT (ID, NAME, GRAMS) values (3, 'libra', 328);

-- T_INGREDIENT values
insert into T_INGREDIENT (ID, NAME) values (1, 'chocolate');
insert into T_INGREDIENT (ID, NAME) values (2, 'cookie');

-- T_RECIPE values
insert into T_RECIPE (ID, NAME, BAKING_TEMP, BAKING_TIME) values (1, 'chocolate cake', 180, 20);
insert into T_RECIPE (ID, NAME, BAKING_TEMP, BAKING_TIME) values (2, 'chocolate cookies', 220, 10);
insert into T_RECIPE (ID, NAME, BAKING_TEMP, BAKING_TIME) values (3, 'fish and chips', 180, 5);

-- T_RECIPE_INGREDIENT values
insert into T_RECIPE_INGREDIENT (ID, RECIPE_ID, INGREDIENT_ID, AMOUNT, UNIT_ID) values (1, 1, 1, 1, 2);
insert into T_RECIPE_INGREDIENT (ID, RECIPE_ID, INGREDIENT_ID, AMOUNT, UNIT_ID) values (2, 2, 1, 100, 1);
insert into T_RECIPE_INGREDIENT (ID, RECIPE_ID, INGREDIENT_ID, AMOUNT, UNIT_ID) values (3, 2, 2, 200, 1);

