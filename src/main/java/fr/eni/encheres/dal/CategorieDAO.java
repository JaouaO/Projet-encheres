package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;

public interface CategorieDAO {

	/**
	 * Ajoute une catégorie en BDD
	 * 
	 * @param categorie
	 */
	void creerCategorie(Categorie categorie);

	/**
	 * Supprime une catégorie en BDD
	 * 
	 * @param idCategorie
	 */
	void supprimerCategorie(long idCategorie);

	/**
	 * modifie la catégorie à l'iD renseignée en BDD
	 * 
	 * @param idCategorie
	 */
	void modifierCategorie(long idCategorie);

	/**
	 * retourne la catégorie correspondante à l'ID renseigné depuis la BDD
	 * 
	 * @param Categorie
	 * @return
	 */
	Categorie consulterParId(long Categorie);

	/**
	 * Ajoute un article à la table des Articles correspondant à cette catégorie NON
	 * IMPLEMENTEE
	 * 
	 * @param article
	 * @return
	 */
	Categorie ajouterArticle(Article article);

	/**
	 * retourne la liste des catégories existantes en BDD
	 * 
	 * @return
	 */
	List<Categorie> consulterTout();

}
