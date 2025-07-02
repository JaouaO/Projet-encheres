package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Enchere;

public interface EnchereService {

	void creerArticle(Article article);

	void mettreEnVente(long idArticle);

	void annulerVente(long idArticle);

	void finaliserVente(long idArticle);

	void vendreArticle(long idArticle);

	Article consulterParId(long idArticle);

	List<Article> consulterTout();

	List<Article> consulterParRecherche(String recherche);

	List<Article> consulterParCategorie(long idCategorie);

	List<Article> consulterParEtat(String etat);

	
	void ajouterEnchere(Enchere enchere);
	
}
