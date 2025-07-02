package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;

public interface CategorieDAO {

     void creerCategorie(Categorie categorie);
     void supprimerCategorie(long idCategorie);
     void modifierCategorie(long idCategorie);
     Categorie consulterParId(long Categorie);
     Categorie ajouterArticle(Article article);


}
