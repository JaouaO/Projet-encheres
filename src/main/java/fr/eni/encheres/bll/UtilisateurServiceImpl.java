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
       utilisateurDAO.insert(utilisateur);
    }

    @Override
    public void modifierUtilisateur(Utilisateur utilisateur, long idUtilisateur) {
      Utilisateur existant = utilisateurDAO.selectById(idUtilisateur);
            utilisateur.setId(idUtilisateur); 
            utilisateurDAO.update(utilisateur);
        
    }

    @Override
    public void supprimerUtilisateur(long idUtilisateur) {
       utilisateurDAO.delete(idUtilisateur);
    }

    @Override
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurDAO.selectAll();
    }

    @Override
    public Utilisateur consulterParEnchere(long idEnchere) {
        return utilisateurDAO.selectByEnchereId(idEnchere);
    }

    @Override
    public Utilisateur consulterParId(long idUtilisateur) {
        return utilisateurDAO.selectById(idUtilisateur);
    }


    @Override
    public void acheterCredits(int nbAchat) {
        Utilisateur utilisateur = utilisateurdao.selectById(idUtilisateur);
        int nouveauCredit = utilisateur.getCredit() + nbAchat;
        utilisateur.setCredit(nouveauCredit);
        utilisateurDAO.update(utilisateur);
    }

}




