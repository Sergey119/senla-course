SELECT p.maker, AVG(l.screen) AS avg_screen_size
FROM Laptop l JOIN Product p ON l.model = p.model
GROUP BY p.maker;