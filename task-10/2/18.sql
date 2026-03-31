SELECT p.maker, pr.price
FROM Printer pr JOIN Product p ON pr.model = p.model
WHERE pr.color = 'y'
  AND pr.price = (
    SELECT MIN(price)
    FROM Printer
    WHERE color = 'y'
);