WITH min_ram AS (
    SELECT MIN(ram) AS min_ram_value
    FROM PC
), fastest_among_min_ram AS (
    SELECT MAX(speed) AS max_speed_value
    FROM PC
    WHERE ram = (SELECT min_ram_value FROM min_ram)
)
SELECT DISTINCT p_pr.maker
FROM Product p_pr
    JOIN Printer pr ON p_pr.model = pr.model
    JOIN Product p_pc ON p_pr.maker = p_pc.maker AND p_pc.type = 'PC'
    JOIN PC pc ON p_pc.model = pc.model
WHERE pc.ram = (SELECT min_ram_value FROM min_ram) AND pc.speed = (SELECT max_speed_value FROM fastest_among_min_ram);