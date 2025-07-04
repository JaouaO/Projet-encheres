DROP TABLE IF EXISTS Enchere;
DROP TABLE IF EXISTS Retrait;
DROP TABLE IF EXISTS Article;
DROP TABLE IF EXISTS Utilisateur;
DROP TABLE IF EXISTS Categorie;


IF OBJECT_ID('Enchere', 'U') IS NOT NULL DROP TABLE Enchere;
IF OBJECT_ID('Retrait', 'U') IS NOT NULL DROP TABLE Retrait;
IF OBJECT_ID('Article', 'U') IS NOT NULL DROP TABLE Article;
IF OBJECT_ID('Utilisateur', 'U') IS NOT NULL DROP TABLE Utilisateur;
IF OBJECT_ID('Categorie', 'U') IS NOT NULL DROP TABLE Categorie;


SET LANGUAGE us_english;

--  Categorie
CREATE TABLE Categorie (
    id BIGINT IDENTITY(1,1) PRIMARY KEY, 
    libelle VARCHAR(250) NOT NULL
);

--  Utilisateur
CREATE TABLE Utilisateur (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    pseudo VARCHAR(200) NOT NULL,
    nom VARCHAR(200) NOT NULL,
    prenom VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    telephone VARCHAR(20),
    rue VARCHAR(200) NOT NULL,
    code_postal VARCHAR(10) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    mot_de_passe VARCHAR(200) NOT NULL,
    credit INT NOT NULL DEFAULT 0 CHECK (credit >= 0),
    administrateur BIT NOT NULL DEFAULT 0
);

--  Article
CREATE TABLE Article (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nom VARCHAR(200) NOT NULL,
    description TEXT,
    date_debut DATETIME NOT NULL, 
    date_fin DATETIME NOT NULL,
    mise_a_prix INT NOT NULL,
    prix_vente INT,
    etat_vente VARCHAR(50) NOT NULL,
    id_vendeur BIGINT NOT NULL,
    id_categorie BIGINT NOT NULL,
    chemin_img VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_vendeur) REFERENCES Utilisateur(id),
    FOREIGN KEY (id_categorie) REFERENCES Categorie(id)
);

--  Table Retrait
CREATE TABLE Retrait (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    rue VARCHAR(255) NOT NULL,
    code_postal VARCHAR(10) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    id_article BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (id_article) REFERENCES Article(id)
);

--  Enchere
CREATE TABLE Enchere (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    date_enchere DATETIME NOT NULL DEFAULT GETDATE(),
    montant_enchere INT NOT NULL,
    id_utilisateur BIGINT NOT NULL,
    id_article BIGINT NOT NULL,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id),
    FOREIGN KEY (id_article) REFERENCES Article(id)
);
-- Contrainte sur les choix des états des vente

ALTER TABLE Article ADD CONSTRAINT chekEtatVente CHECK (etat_vente IN ('en_cours', 'terminee', 'non_debutee', 'livree'));