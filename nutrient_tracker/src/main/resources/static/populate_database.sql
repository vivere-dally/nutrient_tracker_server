truncate table meals_foods CASCADE;
truncate table foods_nutrients CASCADE;
truncate table nutrients CASCADE;
truncate table foods CASCADE;
truncate table meals CASCADE;

insert into nutrients(id, name, value) values
(-1, 'protein', 1.1),
(-2, 'lipids', 0.3),
(-3, 'sodium', 0.3),
(-4, 'carbohydrates', 23),
(-5, 'protein', 0.3),
(-6, 'lipids', 0.2),
(-7, 'sodium', 0.001),
(-8, 'protein', 1.1);

insert into foods(id, description, name) values
(-1, 'grape', 'banana'),
(-2, 'red', 'apple');

insert into meals(id, comment, meal_date) values
(-1, 'delicious', '2020-6-1 18:23:00'),
(-2, 'salty', '2020-7-24 18:23:00');

insert into foods_nutrients(food_id, nutrient_id) values
(-1, -1),
(-1, -2),
(-1, -3),
(-1, -4),
(-2, -5),
(-2, -6),
(-2, -7),
(-2, -8);

insert into meals_foods(meal_id, food_id) values
(-1, -1),
(-1, -2),
(-2, -1),
(-2, -2);
