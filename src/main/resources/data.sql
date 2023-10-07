

-- Insertion d'un admin
INSERT INTO admin (username, password)
VALUES ('admin1', 'mot_de_passe_admin'), ('admin2', 'password2'), ('admin3', 'password3');

-- Insertion de catégories
INSERT INTO category (label) VALUES ('Alimentation'), ('Électronique'), ('Vêtements'), ('Jardinage');

-- Insertion de produits
INSERT INTO product (label, description, prix, category_id, admin_id, promotion_id)
VALUES
     ('Produit 1', 'Description du produit 1', 19.99, 1, 1, 1),
     ('Produit 2', 'Description du produit 2', 29.99, 2, 2, 1),
     ('Produit 3', 'Description du produit 3', 9.99, 3, 3, 2);

-- Insertion d'une promotion
INSERT INTO promotion (start_date, end_date, discount_percentage) VALUES ('2023-01-01', '2023-02-01', 10.0);
