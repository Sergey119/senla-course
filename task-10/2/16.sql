SELECT pc1.model AS model_larger, pc2.model AS model_smaller, pc1.speed, pc1.ram
FROM PC pc1 JOIN PC pc2 ON pc1.speed = pc2.speed AND pc1.ram = pc2.ram AND pc1.model > pc2.model;