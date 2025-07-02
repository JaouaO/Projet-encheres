package fr.eni.encheres.bll;

import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.ArticleDAO;

public class AdminServiceImpl implements AdminService {

    private final UtilisateurDAO utilisateurDAO;
    private final CategorieDAO categorieDAO;
    private final ArticleDAO articleDAO;

     public AdminServiceImpl(UtilisateurDAO utilisateurDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO) {
        this.utilisateurDAO = utilisateurDAO;
        this.categorieDAO = categorieDAO;
        this.articleDAO = articleDAO;
    }

    @Override
    public void supprimerUtilisateur(long idUtilisateur) {
        utilisateurDAO.supprimerUtilisateur(idUtilisateur);
    }

    @Override
    public void desactiverUtilisateur(long idUtilisateur) {
        utilisateurDAO.desactiverUtilisateur(idUtilisateur);
    }

    @Override
    public void creerCategorie(Categorie categorie) {
        categorieDAO.Categorie(categorie); 
    }

    @Override
    public void supprimerCategorie(long idCategorie) {
        categorieDAO.supprimerCategorie(idCategorie);
    }

    @Override
    public void modifierCategorie(long idCategorie) {
        categorieDAO.modifierCategorie(idCategorie);
    }

    @Override
    public void annulerVente(long idArticle) {
        articleDAO.annulerVente(idArticle);
    }
}
