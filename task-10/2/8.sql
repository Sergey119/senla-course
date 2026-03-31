SELECT DISTINCT p1.maker
FROM Product p1
WHERE p1.type = 'PC'
  AND p1.maker NOT IN (
    SELECT DISTINCT p2.maker
    FROM Product p2
    WHERE p2.type = 'Laptop'
);