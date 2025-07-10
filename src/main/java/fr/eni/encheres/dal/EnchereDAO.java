package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Enchere;

import java.util.List;


public interface EnchereDAO {

    /**Ajoute une nouvelle enchere en BDD
     * @param enchere
     */
    void ajouterEnchere(Enchere enchere);

    /**retourne la liste des enchere effectuées par l'utilisateur dont l'ID est renseigné depuis la BDD
     * @param idUtilisateur
     * @return
     */
    List<Enchere> consulterParUtilisateur(long idUtilisateur);

    /**retourne la liste des enchere effectuées pour l'article dont l'ID est renseigné depuis la BDD
     * @param idArticle
     * @return
     */
    List<Enchere> consulterParArticle(long idArticle);

    /**retourne toutes les enchères existantes en BDD
     * @return
     */
    List<Enchere> consulterTout();
    
    /**retourne true si l'article dont l'iD est renseigné à une enchère effectuée par l'utilisateur dont l'ID est renseigné dans la BDD
     * @param idUtilisateur
     * @param idArticle
     * @return
     */
    boolean hasEnchereUtilisateur(long idUtilisateur, long idArticle);

    /**Supprime l'enchère dans la BDD
     * @param enchere
     */
    void annulerEnchere(Enchere enchere);
    

    
    
}
