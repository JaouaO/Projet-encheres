-- utilisateurs

SET LANGUAGE us_english;

-- catégories
INSERT INTO Categorie (libelle) VALUES
                                    ('Moyen de transport'),
                                    ('Electronique'),
                                    ('Vêtements');


-- utilisateurs
INSERT INTO Utilisateur (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES
('MartinLeLapin', 'Martin', 'Paul', 'paul.martin@mail.com', '0600000001', '12 rue des Ventes', '44000', 'Nantes', 'pass123', 100, 0),
('AliceLaMalice', 'Durand', 'Alice', 'alice.durand@mail.com', '0600000002', '5 avenue Achats', '75000', 'Paris', 'pass456', 100, 0),
('SuperAdmin', 'Admin', 'Super', 'admin@mail.com', '0600000003', '1 rue Admin', '69000', 'Lyon', 'adminpass', 100, 1);



-- articles
INSERT INTO Article (nom, description, date_debut, date_fin, mise_a_prix, prix_vente, etat_vente, id_vendeur, id_categorie, chemin_img) VALUES
                                                                                                                                            ('Voiture volante Audi Pop.Up Next', 'Année 2025, très bon état, seulement 50000km au compteur', '2025-06-29 10:00:00', '2025-07-05 10:00:00', 100, NULL, 'EN_COURS', 1, 1, 'PopUpNext'),
                                                                                                                                            ('Smartphone kit LEGO', 'Issu du projet Ara, il ne manque aucune pièce !', '2025-06-20 09:00:00', '2025-06-30 18:00:00', 20, 35, 'TERMINEE', 2, 2, 'smartphoneLEGO'),
                                                                                                                                            ('Veste auto-nettoyante', 'Veste taille M comme neuve, mécanisme auto-nettoyage performant', '2025-05-28 15:00:00', '2025-06-10 15:00:00', 50, NULL, 'LIVREE', 3, 3, 'vesteAutonettoyante'),
                                                                                                                                            ('Drone internet solaire', 'De marque Aquila, panneau solaire changé récemment', '2025-07-25 10:00:00','2025-08-10 18:00:00', 75, NULL, 'NON_DEBUTEE', 1, 2, 'DroneSolaire'),
                                                                                                                                            ('Vélo Cyclotron', 'Dernier vélo de la gamme, connecté et sansa rayon', '2025-07-05 09:30:00', '2025-09-30 19:00:00', 150, null, 'EN_COURS', 2, 1, 'Cyclotron' ),
                                                                                                                                            ('Montre connectée holographique', 'Quelques rayures mais en bon état', '2025-07-04 09:30:00', '2025-09-20 19:00:00', 60, null, 'EN_COURS', 3, 2, 'montreHolographique' ),
                                                                                                                                            ('Moto BMW', 'Super moto, vitesse de pointe 400km/h sur circuit', '2025-04-05 09:30:00', '2025-04-30 19:00:00', 100, 230, 'TERMINEE', 3, 1, 'Moto' ),
                                                                                                                                            ('Sweat-shirt couleur variable', 'Change ed colori selon votre humeur mais peut aussi être réglé selon changements horaires!', '2025-08-05 08:00:00', '2025-11-30 21:00:00', 80, null, 'NON_DEBUTEE', 1, 3, 'SweatCameleon' );






-- retraits
INSERT INTO Retrait (rue, code_postal, ville, id_article) VALUES
                                                              ('12 rue des Ventes', '44000', 'Nantes', 1),
                                                              ( '5 avenue Achats', '75000', 'Paris',  2),
                                                              ('1 rue Admin', '69000', 'Lyon', 3),
                                                              ('12 rue des Ventes', '44000', 'Nantes', 4);



--  enchères
INSERT INTO Enchere (id_utilisateur, id_article, date_enchere, montant_enchere) VALUES
(2, 1, '2025-06-25 10:00:00', 30),
(2, 3, '2025-06-25 10:00:00', 30),
(2, 4, '2025-06-29 10:00:00', 35),
(3, 1, '2025-06-25 12:00:00', 50),
(3, 2, '2025-06-29 12:00:00', 50);


