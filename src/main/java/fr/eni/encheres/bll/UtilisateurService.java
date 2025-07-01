package fr.eni.encheres.bll;
import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {


    void creerUtilsateur(Utilisateur utilisateur);
    void modifierUtilisateur(Utilisateur utilisateur, long idUtilisateur);
    void supprimerUtilisateur(long idUtilisateur);
    List<Utilisateur> getUtilisateurs();
    Utilisateur consulterParEnchere(long idEnchere);
    Utilisateur consulterParId(long idUtilisateur);
    void acheterCredits(long idUtilisateur, int nbAchat);
}
