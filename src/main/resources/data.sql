

-- Insertion d'un user
INSERT INTO users (lastname, firstname, email, role, username, password)
VALUES
       ('Zakary', 'Jack','jack.z@gmail.com','USER','jack_z', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.'),
       ('Tamota', 'Jhon', 'jhon.t@gmx.com', 'ADMIN','jhon_t', '$2y$10$kp1V7UYDEWn17WSK16UcmOnFd1mPFVF6UkLrOOCGtf24HOYt8p1iC');

-- Insertion de catégories
INSERT INTO categories (label) VALUES ('extenciles'), ('Mécanique'), (' Vêtement professionnelle'), ('Décoration'), ('Electricité');

-- Insertion de produits
INSERT INTO products (label, description, prix, category_id, user_id, promotion_id)
VALUES
     ('Produit 11', 'Description du produit 11', 29.99, 5, 2, 1),
     ('Produit 12', 'Description du produit 12', 33.99, 1, 2, 1),
     ('Produit 13', 'Description du produit 13', 39.99, 2, 3, 2);

-- Insertion d'une promotion
INSERT INTO promotions (start_date, end_date, discount_percentage) VALUES ('2023-01-01', '2023-02-01', 15.0), ('2023-11-01', '2023-11-20', 25.0);

