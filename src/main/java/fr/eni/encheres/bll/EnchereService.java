package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

public interface EnchereService {

	void creerArticle(Article article);

	void mettreEnVente(long idArticle);

	void annulerVente(long idArticle);

	void finaliserVente(long idArticle);

	void vendreArticle(long idArticle);

	Article consulterArticleParId(long idArticle);

	List<Article> consulterToutArticle();
	
	List<Categorie> consulterToutCategorie();

	List<Article> consulterParRecherche(String recherche);

	List<Article> consulterParCategorie(long idCategorie);

	List<Article> consulterParEtat(String etat);
	
	List<Article> consulterParEtat(String etat, String etatDeux);

	void ajouterEnchere(Enchere enchere);

    Enchere recupererDerniereEnchere(long idArticle);

	List<Article> consulterParCategorieEtRecherche(Long idCategorie, String text);

	Categorie consulterCategorieParId(long idCategorie);

	List<Enchere> consulterToutesEncheres();

	List<Enchere> consulterParIdArticle(long idArticle);

	List<Article> consulterParIdUtilisateurEtEnchereOuverte(long idUtilisateur);

	List<Article> consulterParIdUtilisateurEtEtatVente(long idUtilisateur, String etatVente);
	
	List<Article> consulterArticlesParQuerySQLPersonnalisee(String sql);
	
	boolean aEncheri(long idArticle, long idUtilisateur);



}
