SELECT p.maker, l.speed
FROM Laptop l JOIN Product p ON l.model = p.model
WHERE l.hd >= 100;