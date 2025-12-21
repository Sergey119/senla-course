
DELETE FROM PC;
DELETE FROM Laptop;
DELETE FROM Printer;
DELETE FROM Product;

INSERT INTO Product (maker, model, type) VALUES
('A', 'PC_A_1', 'PC'),
('A', 'PC_A_2', 'PC'),
('A', 'PC_A_3', 'PC'),
('A', 'Laptop_A_1', 'Laptop'),
('A', 'Laptop_A_2', 'Laptop'),
('B', 'PC_B_1', 'PC'),
('B', 'Laptop_B_1', 'Laptop'),
('B', 'Printer_B_1', 'Printer'),
('C', 'PC_C_1', 'PC'),
('C', 'PC_C_2', 'PC'),
('C', 'Laptop_C_1', 'Laptop'),
('C', 'Printer_C_1', 'Printer'),
('D', 'Printer_D_1', 'Printer'),
('D', 'Printer_D_2', 'Printer'),
('E', 'PC_E_1', 'PC'),
('E', 'PC_E_2', 'PC'),
('F', 'Laptop_F_1', 'Laptop'),
('G', 'Printer_G_1', 'Printer'),
('J', 'Laptop_J_1', 'Laptop'),
('H', 'PC_H_1', 'PC'),
('H', 'Laptop_H_1', 'Laptop'),
('I', 'PC_I_1', 'PC'),
('I', 'Laptop_I_1', 'Laptop');

INSERT INTO PC (code, model, speed, ram, hd, cd, price) VALUES
(11, 'PC_A_1', 400, 1024, 20.0, '4x', 200.0),
(12, 'PC_A_2', 450, 2048, 30.0, '12x', 550.0),
(13, 'PC_A_3', 830, 2048, 30.0, '12x', 540.0),
(14, 'PC_B_1', 500, 2048, 40.0, '24x', 650.0),
(15, 'PC_C_1', 600, 1024, 50.0, '24x', 599.0),
(16, 'PC_C_2', 450, 1024, 60.0, '48x', 300.0),
(17, 'PC_E_1', 700, 2048, 80.0, '52x', 800.0),
(18, 'PC_E_2', 700, 2048, 80.0, '52x', 900.0),
(19, 'PC_H_1', 800, 4096, 100.0, '48x', 1200.00),
(20, 'PC_I_1', 900, 8192, 200.0, '52x', 2000.00);

INSERT INTO Laptop (code, model, speed, ram, hd, screen, price) VALUES
(11, 'Laptop_A_1', 450, 1024, 10.0, 11, 900.0),
(12, 'Laptop_A_2', 480, 1024, 1000.0, 12, 800.0),
(13, 'Laptop_B_1', 550, 2048, 20.0, 12, 1100.0),
(14, 'Laptop_C_1', 750, 2048, 30.0, 13, 1500.0),
(15, 'Laptop_J_1', 56, 2048, 30.0, 13, 1500.0),
(16, 'Laptop_F_1', 800, 4096, 40.0, 14, 2000.0),
(17, 'Laptop_H_1', 850, 4096, 150.0, 15, 1500.00),
(18, 'Laptop_I_1', 950, 16384, 500.0, 17, 3000.00);

INSERT INTO Printer (code, model, color, type, price) VALUES
(11, 'Printer_B_1', 'y', 'Jet', 150.0),
(12, 'Printer_C_1', 'n', 'Laser', 180.00),
(13, 'Printer_D_1', 'n', 'Laser', 200.0),
(14, 'Printer_D_2', 'y', 'Jet', 250.0),
(15, 'Printer_G_1', 'n', 'Matrix', 100.0);