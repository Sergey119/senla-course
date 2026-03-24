SELECT model, ram, screen
FROM Laptop
WHERE price > CAST(1000 AS MONEY);