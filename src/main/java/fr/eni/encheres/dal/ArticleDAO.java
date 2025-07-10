package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;

import java.util.List;

public interface ArticleDAO {

	public void creerArticle(Article article);

	public void mettreEnVente(long idArticle);

	public void annulerVente(long idArticle);

	public void vendreArticle(long idArticle);

	public Article consulterParId(long idArticle);

	public List<Article> consulterTout();

	public List<Article> consulterParRecherche(String motRecherche);

	public List<Article> consulterParCategorie(long idCategorie);

	public List<Article> consulterParEtat(String etatVente);

	public List<Article> consulterParEtat(String etatVente, String etatVenteDeux);

	List<Article> consulterParUtiisateurEtEnchereOuverte(long idUtilisateur);

	List<Article> consulterParIdUtilisateurEtEtatVente(long idUtilisateur, String etatVente);

	List<Article> sqlQueryPersonnalisee(String sql);

	List<Article> consulterParCategorieEtNom(Long idCategorie, String recherche);

	void mettreAJourArticle(Article article);

	public boolean hasArticle(long idArticle);

	boolean isArticleEtatOuvert(long idArticle);

	boolean existeArticle(long idArticle);

}
