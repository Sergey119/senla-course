SELECT speed, AVG(price::NUMERIC) AS avg_price
FROM PC
GROUP BY speed;