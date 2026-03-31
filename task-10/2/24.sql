SELECT model
FROM (
    SELECT model, price FROM PC
    UNION ALL
    SELECT model, price FROM Laptop
    UNION ALL
    SELECT model, price FROM Printer
) AS all_products
WHERE price = (SELECT MAX(price) FROM (
    SELECT price FROM PC
    UNION ALL
    SELECT price FROM Laptop
    UNION ALL
    SELECT price FROM Printer
) AS prices);