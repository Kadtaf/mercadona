--Création de la base de données
CREATE DATABASE mercadona_DB
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'fr_FR.UTF-8'
    LC_CTYPE = 'fr_FR.UTF-8';


-- Création de la table users
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       lastname VARCHAR(50) NOT NULL,
                       firstname VARCHAR(50) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Création de la table categories
CREATE TABLE categories (
                          id SERIAL PRIMARY KEY,
                          label VARCHAR(255) NOT NULL
);

-- Création de la table promotions
CREATE TABLE promotions (
                            id SERIAL PRIMARY KEY,
                            start_date DATE NOT NULL,
                            end_date DATE NOT NULL,
                            discount_percentage DECIMAL(5,2) DEFAULT NULL
);

-- Création de la table products
CREATE TABLE products (
                         id SERIAL PRIMARY KEY,
                         label VARCHAR(255) NOT NULL,
                         image VARCHAR(255),
                         description TEXT,
                         prix DECIMAL(10,2) NOT NULL,
                         category_id BIGINT,
                         promotion_id BIGINT DEFAULT NULL,
                         user_id BIGINT NOT NULL,
                         FOREIGN KEY (promotion_id) REFERENCES Promotions(id),
                         FOREIGN KEY (category_id) REFERENCES Categories(id),
                         FOREIGN KEY (user_id) REFERENCES users(id)

);



