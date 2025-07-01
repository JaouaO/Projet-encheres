package fr.eni.encheres.bll;
import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {

    void insert(Utilisateur utilisateur);
    void update(Utilisateur utilisateur);
    void delete(long idUtilisateur);
    Utilisateur selectById(long id);
    Utilisateur selectByEnchereId(long idEnchere);
    List<Utilisateur> selectAll();
    void acheterCredits(long idUtilisateur, int nbAchat);
    
}
