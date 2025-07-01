
package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

import java.util.List;

public class ArticleDAOImpl implements ArticleDAO {
    @Override
    public void creerArticle(Article article) {

    }

    @Override
    public void mettreEnVente(long idArticle) {

    }

    @Override
    public void annulerVente(long idArticle) {

    }

    @Override
    public void vendreArticle(long idArticle, Enchere enchere) {

    }

    @Override
    public Article consulterparId(long idArticle) {
        return null;
    }

    @Override
    public List<Article> consulterTout() {
        return List.of();
    }

    @Override
    public List<Article> consulterParRecherche(String motRecherche) {
        return List.of();
    }

    @Override
    public List<Article> consulterParCategorie(Categorie categorie) {
        return List.of();
    }

    @Override
    public List<Article> consulterParEtat(String etatVente) {
        return List.of();
    }
}