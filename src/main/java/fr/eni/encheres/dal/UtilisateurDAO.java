package fr.eni.encheres.dal;
import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

    public void creerUtilisateur(Utilisateur utilisateur);

    public void modifierUtilisateur(Utilisateur utilisateur, long idUtilisateur);

    public void supprimerUtilisateur(long idUtilisateur);

    public void desactiverUtilisateur(long idUtilisateur);

    public Utilisateur consulterParId (long idUtilisateur);

    public void ajouterCredits(int nbAjout, long idUtilisateur);

    public void retirerCredits(int nbRetire, long idUtilisateur);

 }