package fr.eni.encheres.bll;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {

	    void creerUtilisateur(Utilisateur utilisateur);
	    void modifierUtilisateur(long idUtilisateur);
	    void supprimerUtilisateur(long idUtilisateur);
	    void desactiverUtilisateur(long idUtilisateur);
	    Utilisateur consulterParId(long idUtilisateur);
	    void ajouterCredits(int nbAjout, long idUtilisateur);
	    void retirerCredits(int nbRetire, long idUtilisateur);
		void annulerVenteParUtilisateur(long idArticle, long idUtilisateur);
		Utilisateur verifierConnexion(String pseudo, String motDePasse);
	
}
