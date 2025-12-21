SELECT speed, AVG(price::NUMERIC) AS avg_price
FROM PC
WHERE speed > 600
GROUP BY speed;