SELECT AVG(speed) AS avg_speed
FROM Laptop
WHERE price > CAST(1000 AS MONEY);