package fr.eni.encheres.bll;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {

	/**
	 * Ajoute l'utilisateur renseigné dans la BDD
	 * 
	 * @param utilisateur
	 */
	void creerUtilisateur(Utilisateur utilisateur);

	/**
	 * Modifie l'utilisateur renseigné dans la BDD pour qu'il corresponde à celui
	 * renseigné dans la méthode
	 * 
	 * @param idUtilisateur
	 * @param utilisateur
	 */
	void modifierUtilisateur(long idUtilisateur, Utilisateur utilisateur);

	/**
	 * Supprime l'utilisateur avec l'id renseigné en BDD
	 * 
	 * @param idUtilisateur
	 */
	void supprimerUtilisateur(long idUtilisateur);

	/**
	 * NON IMPLEMENTE
	 * 
	 * @param idUtilisateur
	 */
	void desactiverUtilisateur(long idUtilisateur);

	/**
	 * renvoie l'utilisateur dont l'id correspond à celui renseigné à partir de la
	 * BDD
	 * 
	 * @param idUtilisateur
	 * @return
	 */
	Utilisateur consulterParId(long idUtilisateur);

	/**
	 * ajoute au credit en BDD la quantité renseigné pour l'utilisateur renseigné,
	 * renvoie une exception en cas d'erreur
	 * 
	 * @param nbAjout
	 * @param idUtilisateur
	 */
	void ajouterCredits(int nbAjout, long idUtilisateur);

	/**
	 * soustrait au credit en BDD la quantité renseigné pour l'utilisateur
	 * renseigné, renvoie une exception en cas d'erreur
	 * 
	 * @param nbRetire
	 * @param idUtilisateur
	 */
	void retirerCredits(int nbRetire, long idUtilisateur);

	/**
	 * NON IMPLEMENTE
	 * 
	 * @param idArticle
	 * @param idUtilisateur
	 */
	void annulerVenteParUtilisateur(long idArticle, long idUtilisateur);

	/**
	 * renvoie l'utilisateur dont le pseudo et le motdepasse correspondent au même
	 * utilisateur existant en BDD renvoie null sinon
	 * 
	 * @param pseudo
	 * @param motDePasse
	 * @return
	 */
	Utilisateur verifierConnexion(String pseudo, String motDePasse);

}
