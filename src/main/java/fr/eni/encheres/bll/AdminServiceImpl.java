package fr.eni.encheres.bll;

import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.ArticleDAO;

public class AdminServiceImpl implements AdminService {

    private final UtilisateurDAO utilisateurDAO;
    private final categorieDAO categorieDAO;
    private final ArticleDAO articleDAO;

     public AdminServiceImpl(UtilisateurDAO utilisateurDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO) {
        this.utilisateurDAO = utilisateurDAO;
        this.categorieDAO = categorieDAO;
        this.articleDAO = articleDAO;
    }

    @Override
    public void supprimerUtilisateur(long idUtilisateur) {
       utilisateurDAO.delete(idUtilisateur);
    }

    @Override
    public void desactiverUtilisateur(long idUtilisateur) {
        Utilisateur u = utilisateurDAO.selectById(idUtilisateur);
         utilisateurDAO.update(u);
    }

    @Override
    public void creerCategorie(Categorie categorie) {
        categorieDAO.insert(categorie);
    }

    @Override
    public void supprimerCategorie(long idCategorie) {
        categorieDAO.delete(idCategorie);
    }

    @Override
    public void modifierCategorie(long idCategorie) {
        Categorie c = categorieDAO.selectById(idCategorie);
         categorieDAO.update(c);
    }

    @Override
    public void annulerVente(long idArticle) {
        Article article = articleDAO.selectById(idArticle);
        articleDAO.update(article);
    }

}
