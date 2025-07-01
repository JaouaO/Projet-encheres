package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

public interface CategorieDAO {

    public void Categorie(Categorie categorie);
    public void supprimerCategorie(long idCategorie);
    public int modifierCategorie(long idCategorie);
    Categorie consulterParId(long Categorie);
    public Enchere ajouterArticle(long idArticle);


}
