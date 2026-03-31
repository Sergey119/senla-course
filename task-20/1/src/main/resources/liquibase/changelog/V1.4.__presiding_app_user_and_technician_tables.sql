INSERT INTO app_user (username, password, role)
VALUES
    ('admin',
     '$2a$10$MF03UcTeNAsKYzQlJKV8reBJ3mQZyEMbd87bGAFVcF7UWLOyIy2vq',
     'ADMIN'),
    ('user',
     '$2a$10$qmI8EqBD7rbVYmr0aS3j/.YDcFirsEtJC/i7ApVl/WpI.b2TJUhFC',
     'USER');

INSERT INTO technician (name, specialization, is_available)
VALUES
    ('John Smith', 'Engine Repair', TRUE),
    ('Maria Rodriguez', 'Electrical Systems', TRUE),
    ('Robert Brown', 'Brake Systems', FALSE),
    ('Lisa Wang', 'Transmission', TRUE),
    ('Tom Davis', 'Diagnostics', TRUE);