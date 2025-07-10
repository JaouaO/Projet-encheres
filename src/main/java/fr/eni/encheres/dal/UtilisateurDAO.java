package fr.eni.encheres.dal;
import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

    /**Ajoute l'utilisateur renseigné en BDD
     * @param utilisateur
     */
    public void creerUtilisateur(Utilisateur utilisateur);

    /**Modifie l'utilisateur renseigné en BDD à partir de son ID
     * @param idUtilisateur
     * @param utilisateur
     */
    public void modifierUtilisateur(long idUtilisateur, Utilisateur utilisateur);

    /**Supprime l'utilisateur renseigné en BD
     * @param idUtilisateur
     */
    public void supprimerUtilisateur(long idUtilisateur);

    /**change le statut de l'utilisateur renseigné en BDD, pour le désactiver
     * NON IMPLEMENTE
     * @param idUtilisateur
     */
    public void desactiverUtilisateur(long idUtilisateur);

    /**Retourne l'utilisateur correspondant à l'ID renseigné à partir de la BDD
     * @param idUtilisateur
     * @return
     */
    public Utilisateur consulterParId (long idUtilisateur);

    /**Augmente les crédits existants en BDD pour l'utilisateur renseigné du montant renseigné dans la méthode
     * @param nbAjout
     * @param idUtilisateur
     */
    public void ajouterCredits(int nbAjout, long idUtilisateur);

    /**Diminue les crédits existants en BDD pour l'utilisateur renseigné du montant renseigné dans la méthode
     * @param nbRetire
     * @param idUtilisateur
     */
    public void retirerCredits(int nbRetire, long idUtilisateur);
    
    /**Renvoie les crédits appartenants à l'utilisateur dont l'ID est renseigné à partir de la BDD
     * @param idUtilisateur
     * @return
     */
    int consulterCredit(long idUtilisateur);

    /**retourne l'utilisateur dont l'email correspond à celui renseigné à partir de la BDD
     * @param email
     * @return
     */
    Utilisateur consulterParEmail(String email);

    /**retourne l'utilisateur dont le pseudo correspond à celui renseigné à partir de la BDD
     * @param pseudo
     * @return
     */
    Utilisateur consulterParPseudo(String pseudo);
    
    /**Retourne true s'il existe un utilisateur avec l'ID renseigné dans la BDD
     * @param idUtilisateur
     * @return
     */
    boolean hasUtilisateur(long idUtilisateur);
    


 }