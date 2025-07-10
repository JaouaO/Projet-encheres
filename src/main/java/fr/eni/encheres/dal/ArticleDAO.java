package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;

import java.util.List;

public interface ArticleDAO {

	/**Ajoute l'article à la BDD
	 * @param article
	 */
	public void creerArticle(Article article);

	/**passe le statut de l'article de "non_debutee" à "en_vente" en BDD
	 * @param idArticle
	 */
	public void mettreEnVente(long idArticle);

	/**supprime l'article de la BDD
	 * @param idArticle
	 */
	public void annulerVente(long idArticle);

	/**passe le statut de l'article de "non_debutee" à "terminee" en BDD
	 * @param idArticle
	 */
	public void vendreArticle(long idArticle);

	/**retourne l'article correspondant à l'id renseigné à partir de la BDD
	 * @param idArticle
	 * @return
	 */
	public Article consulterParId(long idArticle);

	/**Retourne la liste des articles en BDD
	 * @return
	 */
	public List<Article> consulterTout();

	/**renvoie la liste des articles en BDD qui contienne la recherche dans leur nom
	 * @param motRecherche
	 * @return
	 */
	public List<Article> consulterParRecherche(String motRecherche);

	/**retourne la liste des articles qui ont comme categorie la categorie resneignée
	 * @param idCategorie
	 * @return
	 */
	public List<Article> consulterParCategorie(long idCategorie);

	/**retourne la liste des articles qui ont comme état l'état resneigné
	 * @param etatVente
	 * @return
	 */
	public List<Article> consulterParEtat(String etatVente);

	/**retourne la liste des articles qui ont comme état un des deux état renseigné
	 * @param etatVente
	 * @param etatVenteDeux
	 * @return
	 */
	public List<Article> consulterParEtat(String etatVente, String etatVenteDeux);

	/**reetourne la liste des articles qui ont la catégorie renseignée et qui contiennent
	 * dans leur nom le mot renseigné
	 * @param idCategorie
	 * @param motRecherche
	 * @return
	 */
	List<Article> consulterParCategorieEtNom(Long idCategorie, String motRecherche);

	/**retourne la liste des articles dont l'état est "en_cours" et qui ne sont pas mis en vente par l'utilisateur renseigné
	 * @param idUtilisateur
	 * @return
	 */
	List<Article> consulterParUtiisateurEtEnchereOuverte(long idUtilisateur);

	/**retourne la liste des articles dont l'état correspond à l'état renseigné 
	 * et qui ne sont pas mis en vente par l'utilisateur renseigné
	 * @param idUtilisateur
	 * @param etatVente
	 * @return
	 */
	List<Article> consulterParIdUtilisateurEtEtatVente(long idUtilisateur, String etatVente);
	
	/**retourne une liste d'article à partir d'une query SQL rentrée en paramètre
	 * @param sql
	 * @return
	 */
	List<Article> sqlQueryPersonnalisee(String sql);

	public List<Article> consulterParCategorieEtNom(Long idCategorie, String motRecherche);

    /**Met à jour l'article en mémoire à partir de son statut dans la BDD
     * @param article
     */
    void mettreAJourArticle(Article article);

    /**retourne true si un article avec l'id renseigné existe en BDD
     * @param idArticle
     * @return
     */
    public boolean hasArticle(long idArticle);
    
    /**retourne true si un article avec l'id renseigné à l'état "en_cours" en  BDD
     * @param idArticle
     * @return
     */
    boolean isArticleEtatOuvert(long idArticle);


	/**je crois que c'est un doublon non utilisé
     * @param idArticle
     * @return
     */
    boolean existeArticle(long idArticle);



    

}
