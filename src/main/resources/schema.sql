

-- Création de la table Admin
CREATE TABLE Admins (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Création de la table Category
CREATE TABLE Categories (
                          id SERIAL PRIMARY KEY,
                          label VARCHAR(255) NOT NULL
);

-- Création de la table Product
CREATE TABLE Products (
                         id SERIAL PRIMARY KEY,
                         label VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10,2) NOT NULL,
                         image VARCHAR(255),
                         category_id BIGINT,
                         promotion_id BIGINT DEFAULT NULL,
                         admin_id BIGINT NOT NULL,
                         FOREIGN KEY (promotion_id) REFERENCES Promotions(id),
                         FOREIGN KEY (category_id) REFERENCES Categories(id),
                         FOREIGN KEY (admin_id) REFERENCES Admins(id)

);

-- Création de la table Promotion
CREATE TABLE Promotion (
                           id SERIAL PRIMARY KEY,
                           start_date DATE NOT NULL,
                           end_date DATE NOT NULL,
                           discount_percentage DECIMAL(5,2) DEFAULT NULL
);