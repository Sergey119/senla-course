SELECT p.maker, COUNT(DISTINCT pc.model) AS number_of_models
FROM Product p JOIN PC pc ON p.model = pc.model
WHERE p.type = 'PC'
GROUP BY p.maker
HAVING COUNT(DISTINCT pc.model) >= 3;