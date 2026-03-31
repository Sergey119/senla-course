SELECT model, speed, hd
FROM PC
WHERE price < CAST(500 AS MONEY);