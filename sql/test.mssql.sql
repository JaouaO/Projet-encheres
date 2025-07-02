-- utilisateurs

SET LANGUAGE us_english;
INSERT INTO Utilisateur (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES
('MartinLeLapin', 'Martin', 'Paul', 'paul.martin@mail.com', '0600000001', '12 rue des Ventes', '44000', 'Nantes', 'pass123', 100, 0),
('AliceLaMalice', 'Durand', 'Alice', 'alice.durand@mail.com', '0600000002', '5 avenue Achats', '75000', 'Paris', 'pass456', 100, 0),
('SuperAdmin', 'Admin', 'Super', 'admin@mail.com', '0600000003', '1 rue Admin', '69000', 'Lyon', 'adminpass', 100, 1);

-- catégories
INSERT INTO Categorie (libelle) VALUES
('Moyen de transport'),
('Electronique'),
('Vêtements');

-- articles
INSERT INTO Article (nom, description, date_debut, date_fin, mise_a_prix, prix_vente, etat_vente, id_vendeur, id_categorie) VALUES
('Voiture volante Audi Pop.Up Next', 'Année 2025, très bon état, seulement 50000km au compteur', '2025-06-29 10:00:00', '2025-07-05 10:00:00', 100, NULL, 'EN_COURS', 1, 1),
('Smartphone kit LEGO', 'Issu du projet Ara, il ne manque aucune pièce !', '2025-06-20 09:00:00', '2025-06-30 18:00:00', 20, 35, 'TERMINEE', 1, 2),
('Veste auto-nettoyante', 'Veste taille M comme neuve, mécanisme auto-nettoyage performant', '2025-05-28 15:00:00', '2025-06-10 15:00:00', 50, NULL, 'LIVREE', 1, 3),
('Drone internet solaire', 'De marque Aquila, panneau solaire changé récemment', '2025-07-25 10:00:00','2025-08-10 18:00:00', 75, NULL, 'NON_DEBUTEE', 1, 2);

-- retraits 
INSERT INTO Retrait (rue, code_postal, ville, id_article) VALUES
('12 rue des Ventes', '44000', 'Nantes', 1),
('12 rue des Ventes', '44000', 'Nantes', 2),
('12 rue des Ventes', '44000', 'Nantes', 3),
('12 rue des Ventes', '44000', 'Nantes', 4);

--  enchères
INSERT INTO Enchere (id_utilisateur, id_article, date_enchere, montant_enchere) VALUES
(2, 2, '2025-06-25 10:00:00', 30),
(2, 2, '2025-06-29 12:00:00', 35);


