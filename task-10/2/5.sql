SELECT model, speed, hd
FROM PC
WHERE (cd = '12x' OR cd = '24x') AND price < CAST(600 AS MONEY);