SELECT p.maker, MAX(pc.price) AS max_price
FROM PC pc JOIN Product p ON pc.model = p.model
GROUP BY p.maker;