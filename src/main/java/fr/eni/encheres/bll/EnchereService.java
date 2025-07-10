package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.exceptions.BusinessException;

public interface EnchereService {

	/**
	 * Ajoute l'article renseigne dans la BDD
	 * 
	 * @param article
	 */
	void creerArticle(Article article);

	/**
	 * Fait passer l'etat de tous les articles dont la l'état est "non_debutee" et
	 * la date précède la date actuelle à l'état "en_cours"
	 */
	void mettreEnVente();

	/**
	 * Supprime un article dont l'etat est non_debutee
	 * 
	 * @param idArticle
	 */
	void annulerVente(long idArticle);

	/**
	 * Si l'article dont l'ID est renseigné a eu des enchères, vent l'article à
	 * partir de la méthode "vendreArticle", sinon, annule la vente.
	 * 
	 * @param idArticle
	 */
	void finaliserVente(long idArticle);

	/**
	 * Fait passer l'état de l'article dont l'id est renseigné de "en_cours" à
	 * "terminee" en BDD
	 * 
	 * @param idArticle
	 */
	void vendreArticle(long idArticle);

	/**
	 * retourne l'article dont l'ID correspond à celui renseigné en BDD
	 * 
	 * @param idArticle
	 * @return
	 */
	Article consulterArticleParId(long idArticle);

	/**
	 * Retourne la liste des articles existants en BDD
	 * 
	 * @return
	 */
	List<Article> consulterToutArticle();

	/**
	 * retourne la liste des catégories existantes en BDD
	 * 
	 * @return
	 */
	List<Categorie> consulterToutCategorie();

	/**
	 * retourne la liste des articles existants en BDD dont le nom contien la
	 * recherche
	 * 
	 * @param recherche
	 * @return
	 */
	List<Article> consulterParRecherche(String recherche);

	/**
	 * retourne la liste des articles dont la categorie correspond à celle dont l'id
	 * renseigné à partir de la BDD
	 * 
	 * @param idCategorie
	 * @return
	 */
	List<Article> consulterParCategorie(long idCategorie);

	/**
	 * retourne la liste des articles dont l'état correpond à celui renseigné à
	 * partir de la BDD
	 * 
	 * @param etat
	 * @return
	 */
	List<Article> consulterParEtat(String etat);

	/**
	 * retourne la liste des articles dont un des deux états correpond à ceux
	 * renseignés à partir de la BDD
	 * 
	 * @param etat
	 * @param etatDeux
	 * @return
	 */
	List<Article> consulterParEtat(String etat, String etatDeux);

	/**
	 * ajoute une enchère à la BDD, et renvoie une exception s'il y a une erreur
	 * 
	 * @param enchere
	 * @throws BusinessException
	 */
	void ajouterEnchere(Enchere enchere) throws BusinessException;

	/**
	 * retourne la dernière enchère effectuée pour l'article dont l'id est renseigné
	 * à partir de la BDD
	 * 
	 * @param idArticle
	 * @return
	 */
	Enchere recupererDerniereEnchere(long idArticle);

	/**
	 * retourne la liste des articles qui ont la catégorie renseignée et qui
	 * contiennent dans leur nom le mot renseigné
	 * 
	 * @param idCategorie
	 * @param text
	 * @return
	 */
	List<Article> consulterParCategorieEtRecherche(Long idCategorie, String text);

	/**
	 * retourne la catégorie qui a l'id renseigné à partir de la BDD
	 * 
	 * @param idCategorie
	 * @return
	 */
	Categorie consulterCategorieParId(long idCategorie);

	/**
	 * retourne la liste des enchères existantes en BDD
	 * 
	 * @return
	 */
	List<Enchere> consulterToutesEncheres();

	/**
	 * retourne la liste des enchères effectuées sur l'article dont l'id est
	 * renseigné à partir de la BDD
	 * 
	 * @param idArticle
	 * @return
	 */
	List<Enchere> consulterParIdArticle(long idArticle);

	/**
	 * retourne la liste des articles dont l'utilisateur vendeur n'est pas celui
	 * dont l'iD est renseigné dans la méthode et dont l'état est "en_cours"
	 * 
	 * @param idUtilisateur
	 * @return
	 */
	List<Article> consulterParIdUtilisateurEtEnchereOuverte(long idUtilisateur);

	/**
	 * retourne la liste des articles dont l'utilisateur vendeur n'est pas celui
	 * dont l'iD est renseigné dans la méthode et dont l'état correspond à celui
	 * renseigné
	 * 
	 * @param idUtilisateur
	 * @param etatVente
	 * @return
	 */
	List<Article> consulterParIdUtilisateurEtEtatVente(long idUtilisateur, String etatVente);

	/**
	 * retourne une liste d'article à partir d'une query sql renseignée dans la
	 * méthode
	 * 
	 * @param sql
	 * @return
	 */
	List<Article> consulterArticlesParQuerySQLPersonnalisee(String sql);

	/**
	 * retourne true si l'utilisateur renseigné à enchéri pour l'article renseigné
	 * 
	 * @param idArticle
	 * @param idUtilisateur
	 * @return
	 */
	boolean aEncheri(long idArticle, long idUtilisateur);

	/**
	 * met à jour l'article dans la BDD à partir de son ID et de ce qui est
	 * renseigné dans la méthode
	 * 
	 * @param article
	 */
	void mettreAJourArticle(Article article);

	/**
	 * ajoute au credit en BDD la quantité renseigné pour l'utilisateur renseigné,
	 * renvoie une exception en cas d'erreur
	 * 
	 * @param nbAjout
	 * @param idUtilisateur
	 * @param be
	 */
	void ajouterCredits(int nbAjout, long idUtilisateur, BusinessException be);

	/**
	 * soustrait au credit en BDD la quantité renseigné pour l'utilisateur
	 * renseigné, renvoie une exception en cas d'erreur
	 * 
	 * @param nbRetire
	 * @param idUtilisateur
	 * @param be
	 */
	void retirerCredits(int nbRetire, long idUtilisateur, BusinessException be);

	/**
	 * renvoie true si l'id correspond à un article en BDD, renvoie une exception en
	 * cas d'erreur
	 * 
	 * @param idArticle
	 * @param be
	 * @return
	 */
	boolean hasArticle(long idArticle, BusinessException be);

	/**
	 * renvoie true si l'id renseigné correpond à un utilisateur en BDD, renvoie une
	 * exception en cas d'erreur
	 * 
	 * @param idUtilisateur
	 * @param be
	 * @return
	 */
	boolean hasUtilisateur(long idUtilisateur, BusinessException be);

	/**
	 * renvoie true si le credit de l'utilisateur dont l'id est renseigné est
	 * supérieur ou égal à la quantité renseignée dans la BDD renvoie une exception
	 * sinon
	 * 
	 * @param montant
	 * @param idUtilisateur
	 * @param be
	 * @return
	 */
	boolean isCreditSuffisant(int montant, long idUtilisateur, BusinessException be);

	/**
	 * renvoie true si le montant renseigné est supérieur à la dernière enchère
	 * existant en BDD pour l'article dont l'iD est renseigné, renvoie une exception
	 * sinon
	 * 
	 * @param montant
	 * @param idArticle
	 * @param be
	 * @return
	 */
	boolean isOffreSuperieureDerniereEnchere(int montant, long idArticle, BusinessException be);

	/**
	 * renvoie true si le montant renseigné est supérieur à la mise à prix existante
	 * pour l'article en BDD pour l'article dont l'iD est renseigné, renvoie une
	 * exception sinon
	 * 
	 * @param montant
	 * @param idArticle
	 * @param be
	 * @return
	 */
	boolean isOffreSupérieureMAP(int montant, long idArticle, BusinessException be);

	/**
	 * retourne true si l'article resneigné peut être enchéri, càd si son état est
	 * en_cours
	 * 
	 * @param idArticle
	 * @param be
	 * @return
	 */
	boolean isEnchereOuverte(long idArticle, BusinessException be);

	/**
	 * retourne true si l'article a au moins une enchere en BDD
	 * 
	 * @param idArticle
	 * @return
	 */
	boolean hasAutreEnchere(long idArticle);

	/**
	 * retourne true si l'id_vendeur de l'article en BDD correspond à l'id
	 * utilisateur renseigné
	 * 
	 * @param idArticle
	 * @param idUtilisateur
	 * @return
	 */
	boolean verifierProprietaireArticle(Long idArticle, Long idUtilisateur);

	/**
	 * je crois que c'est un doublon non-utilisé
	 * 
	 * @param idArticle
	 * @return
	 */
	boolean existeArticle(long idArticle);

}
