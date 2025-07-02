package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

import java.util.List;

public interface ArticleDAO {

    public void creerArticle(Article article) ;

    public void mettreEnVente(long idArticle) ;

    public void annulerVente(long idArticle) ;

    public void vendreArticle(long idArticle) ;

    public Article consulterparId(long idArticle) ;

    public List<Article> consulterTout() ;

    public List<Article> consulterParRecherche(String motRecherche) ;

    public List<Article> consulterParCategorie(Categorie categorie) ;

    public List<Article> consulterParEtat(String etatVente) ;
}
