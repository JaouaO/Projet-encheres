package fr.eni.encheres.bll;

import fr.eni.encheres.bo.Categorie;

public interface AdminService {

//RIEN ICI N'EST IMPLEMENTE
    void supprimerUtilisateur(long idUtilisateur);
    void desactiverUtilisateur(long idUtilisateur);
    void creerCategorie(Categorie categorie);
    void supprimerCategorie(long idCategorie);
    void modifierCategorie(long idCategorie);
    void annulerVente(long idArticle);


}