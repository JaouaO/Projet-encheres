package fr.eni.encheres.dal;
import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

    public void creerUtilisateur(Utilisateur utilisateur);

    public void modifierUtilisateur(long idUtilisateur, Utilisateur utilisateur);

    public void supprimerUtilisateur(long idUtilisateur);

    public void desactiverUtilisateur(long idUtilisateur);

    public Utilisateur consulterParId (long idUtilisateur);

    public void ajouterCredits(int nbAjout, long idUtilisateur);

    public void retirerCredits(int nbRetire, long idUtilisateur);
    
    int consulterCredit(long idUtilisateur);

    Utilisateur consulterParEmail(String email);

    Utilisateur consulterParPseudo(String pseudo);
    
    boolean hasUtilisateur(long idUtilisateur);
    


 }