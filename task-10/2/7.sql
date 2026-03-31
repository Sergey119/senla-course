SELECT p.model,
       CASE
           WHEN pc.price IS NOT NULL THEN pc.price
           WHEN l.price IS NOT NULL THEN l.price
           ELSE pr.price
           END AS price
FROM Product p LEFT JOIN PC pc ON p.model = pc.model
    LEFT JOIN Laptop l ON p.model = l.model
    LEFT JOIN Printer pr ON p.model = pr.model
WHERE p.maker = 'B';