SELECT DISTINCT p.maker
FROM Product p JOIN Printer pr ON p.model = pr.model;
