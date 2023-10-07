

-- Création de la table Admin
CREATE TABLE admins (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Création de la table Category
CREATE TABLE categories (
                          id SERIAL PRIMARY KEY,
                          label VARCHAR(255) NOT NULL
);

-- Création de la table Product
CREATE TABLE products (
                         id SERIAL PRIMARY KEY,
                         label VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         image VARCHAR(255),
                         category_id INT,
                         promotion_id INT DEFAULT NULL,
                         admin_id INT NOT NULL,
                         FOREIGN KEY (promotion_id) REFERENCES promotions(id),
                         FOREIGN KEY (category_id) REFERENCES categories(id),
                         FOREIGN KEY (admin_id) REFERENCES admins(id)
);

-- Création de la table Promotion
CREATE TABLE promotions (
                           id SERIAL PRIMARY KEY,
                           start_date DATE NOT NULL,
                           end_date DATE NOT NULL,
                           discount_percentage DECIMAL(5,2) DEFAULT NULL
);