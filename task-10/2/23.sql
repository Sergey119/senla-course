SELECT DISTINCT p_pc.maker
FROM Product p_pc
         JOIN PC pc ON p_pc.model = pc.model
         JOIN Product p_laptop ON p_pc.maker = p_laptop.maker AND p_laptop.type = 'Laptop'
         JOIN Laptop l ON p_laptop.model = l.model
WHERE pc.speed >= 750 AND l.speed >= 750;