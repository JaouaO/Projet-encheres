package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Enchere;

import java.util.List;


public interface EnchereDAO {

    void ajouterEnchere(Enchere enchere);

    List<Enchere> consulterParUtilisateur(long idUtilisateur);

    List<Enchere> consulterParArticle(long idArticle);

    List<Enchere> consulterTout();
    
    boolean hasEnchereUtilisateur(long idUtilisateur, long idArticle);

    void annulerEnchere(Enchere enchere);
    

    
    
}
