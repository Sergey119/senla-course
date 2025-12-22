CREATE TABLE Product (
    maker VARCHAR(10) NOT NULL,
    model VARCHAR(50) NOT NULL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE PC (
    code INT NOT NULL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    cd VARCHAR(50) NOT NULL,
    price MONEY,
    FOREIGN KEY (model) REFERENCES Product(model)
);

CREATE TABLE Laptop (
    code INT NOT NULL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    screen TINYINT NOT NULL,
    price MONEY,
    FOREIGN KEY (model) REFERENCES Product(model)
);

CREATE TABLE Printer (
    code INT NOT NULL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    color CHAR(1) NOT NULL,
    type VARCHAR(10) NOT NULL,
    price MONEY,
    FOREIGN KEY (model) REFERENCES Product(model)
);


INSERT INTO Product (maker, model, type) VALUES
('MSI', 'Pro DP180 14th', 'PC'),
('Dell', 'OptiPlex 7010', 'PC'),
('Lenovo', 'ThinkPad T450', 'Laptop'),
('ASUS', 'Vivobook 17 X1704VA', 'Laptop'),
('HP', 'Tank 410', 'Printer'),
('Epson', 'L3150', 'Printer');

INSERT INTO PC (code, model, speed, ram, hd, cd, price) VALUES
(1, 'Pro DP180 14th', 4200, 4096, 512, '52x', 900),
(2, 'OptiPlex 7010', 2800, 8192, 512, '24x', 750);

INSERT INTO Laptop (code, model, speed, ram, hd, screen, price) VALUES
(1, 'ThinkPad T450', 3500, 16384, 512, 14, 1100),
(2, 'Vivobook 17 X1704VA', 2200, 16384, 512, 15, 900);

INSERT INTO Printer (code, model, color, type, price) VALUES
(1, 'Tank 410', 'y', 'Jet', 120),
(2, 'L3150', 'n', 'Jet', 80);

