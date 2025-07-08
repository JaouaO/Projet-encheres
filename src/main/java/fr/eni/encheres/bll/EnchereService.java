package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.exceptions.BusinessException;

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


	void ajouterEnchere(Enchere enchere) throws BusinessException;

    Enchere recupererDerniereEnchere(long idArticle);


	List<Article> consulterParCategorieEtRecherche(Long idCategorie, String text);

	Categorie consulterCategorieParId(long idCategorie);
	
	void retirerCredits(int nbRetire, long idUtilisateur);
	
    boolean hasArticle(long idArticle, BusinessException be);
    
    boolean hasUtilisateur(long idUtilisateur, BusinessException be);
    
    boolean isCreditSuffisant(int montant, Utilisateur utilisateur, BusinessException be);
    
    boolean isOffreSupérieure(int montant, long idArticle, BusinessException be);
    
    boolean isEnchereOuverte(long idArticle, BusinessException be);

}
