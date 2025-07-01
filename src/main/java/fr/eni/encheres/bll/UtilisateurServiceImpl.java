package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDAO;

public class UtilisateurServiceImpl {

 private final UtilisateurDAO utilisateurdao;

    public UtilisateurServiceImpl(UtilisateurDAO utilisateurdao) {
        this.utilisateurdao = utilisateurdao;
    }


    @Override
    public void creerUtilsateur(Utilisateur utilisateur) {
        utilisateurDA.creerUtilisateur(utilisateur);
    }
    

    @Override
    public void modifierUtilisateur(Utilisateur utilisateur, long idUtilisateur) {
      Utilisateur existant = utilisateurdao.selectById(idUtilisateur);
            utilisateur.setId(idUtilisateur); 
            utilisateurDAO.update(utilisateur);
        
    }

    
    @Override
    public void supprimerUtilisateur(long idUtilisateur) {
        utilisateurDAO.supprimerUtilisateur(idUtilisateur);
    }

    @Override
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurDAO.listerUtilisateurs();
    }

    @Override
     public Utilisateur consulterParEnchere(long idEnchere) {
        return utilisateurDAO.consulterParEnchere(idEnchere);
    }

    @Override
    public Utilisateur consulterParId(long idUtilisateur) {
        return utilisateurDAO.consulterParId(idUtilisateur);
    }


    @Override
    public void acheterCredits(long idUtilisateur, int nbAchat) {
        if (nbAchat <= 0) {
            throw new IllegalArgumentException("Le nombre de crédits achetés doit être positif.");
        }
        Utilisateur utilisateur = utilisateurDAO.consulterParId(idUtilisateur);
        if (utilisateur != null) {
            utilisateurDAO.ajouterCredits(nbAchat, idUtilisateur);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l’ID : " + idUtilisateur);
        }
    }

}




