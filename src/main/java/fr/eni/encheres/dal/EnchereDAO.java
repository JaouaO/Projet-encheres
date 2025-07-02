package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Enchere;

import java.util.List;

public interface EnchereDAO {

    List<Enchere> ajouterEnchere(Enchere enchere);

    List<Enchere> consulterParUtilisateur(long idUtilisateur);

    List<Enchere> consulterParArticle(long idArticle);

    List<Enchere> consulterTout();

    int annulerEnchere(Enchere enchere);
}
