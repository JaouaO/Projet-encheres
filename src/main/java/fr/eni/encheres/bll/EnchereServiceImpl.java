package fr.eni.encheres.bll;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.exceptions.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

	private final Log logger = LogFactory.getLog(getClass());
	private ArticleDAO articleDAO;
	private EnchereDAO enchereDAO;
	private CategorieDAO categorieDAO;
	private UtilisateurDAO utilisateurDAO;

	public EnchereServiceImpl(ArticleDAO articleDAO, EnchereDAO enchereDAO, CategorieDAO categorieDAO,
			UtilisateurDAO utilisateurDAO) {
		this.articleDAO = articleDAO;
		this.enchereDAO = enchereDAO;
		this.categorieDAO = categorieDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	@Override
	public void creerArticle(Article article) {
		this.articleDAO.creerArticle(article);

	}

	@Override
	//il s'agit ici de mettre en vente dans le sens où l'article qui a été créé et qui est en état de vente NON_DEBUTEE
	//passe EN_COURS dès que la date de début d'enchère est arrivée
	public void mettreEnVente() {
		List<Article> ventesNonDebutees = articleDAO.consulterParEtat("NON_DEBUTEE");
		for (Article article : ventesNonDebutees) {
			if (article.getDateDebutEnchere().isBefore(LocalDateTime.now())) {
				articleDAO.mettreEnVente(article.getId());
			}
		}
	}

	@Override
	public void annulerVente(long idArticle) {
		this.articleDAO.annulerVente(idArticle);

	}

	@Override
	public void finaliserVente(long idArticle) {

		if (this.enchereDAO.consulterParArticle(idArticle).size() > 0) {
			this.vendreArticle(idArticle);
		} else {
			this.annulerVente(idArticle);
		}

	}

	@Override
	public void vendreArticle(long idArticle) {
		this.articleDAO.vendreArticle(idArticle);

	}

	@Override
	public Article consulterArticleParId(long idArticle) {

		return this.articleDAO.consulterParId(idArticle);
	}

	@Override
	public List<Article> consulterToutArticle() {
		return this.articleDAO.consulterTout();
	}

	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
		return this.categorieDAO.consulterParId(idCategorie);
	}

	@Override
	public List<Enchere> consulterToutesEncheres() {
		return enchereDAO.consulterTout();
	}

	@Override
	public List<Enchere> consulterParIdArticle(long idArticle) {
		return enchereDAO.consulterParArticle(idArticle);
	}

	@Override
	public List<Article> consulterParIdUtilisateurEtEnchereOuverte(long idUtilisateur) {
		return articleDAO.consulterParUtiisateurEtEnchereOuverte(idUtilisateur);
	}

	@Override
	public List<Article> consulterParIdUtilisateurEtEtatVente(long idUtilisateur, String etatVente) {

		return articleDAO.consulterParIdUtilisateurEtEtatVente(idUtilisateur, etatVente);
	}

	@Override
	public Enchere recupererDerniereEnchere(long idArticle) {

		List<Enchere> encheres = this.enchereDAO.consulterParArticle(idArticle);

		encheres.sort(Comparator.comparing(Enchere::getDateEnchere));
		if (encheres.isEmpty()) {
			return null;
		}
		Enchere enchere = encheres.get(encheres.size() - 1);
		enchere.setUtilisateur(this.utilisateurDAO.consulterParId(enchere.getUtilisateur().getId()));
		return encheres.get(encheres.size() - 1);
	}

	@Override
	public List<Categorie> consulterToutCategorie() {
		return this.categorieDAO.consulterTout();
	}

	@Override
	public List<Article> consulterParRecherche(String recherche) {
		// TODO Auto-generated method stub
		return this.articleDAO.consulterParRecherche(recherche);
	}

	@Override
	public List<Article> consulterParCategorie(long idCategorie) {
		return this.articleDAO.consulterParCategorie(idCategorie);
	}

	@Override
	public List<Article> consulterParEtat(String etat) {
		return this.articleDAO.consulterParEtat(etat);

	}	
	
	@Override
	public List<Article> consulterParEtat(String etat, String etatDeux) {
		return this.articleDAO.consulterParEtat(etat, etatDeux);

	}

	@Override
	public List<Article> consulterParCategorieEtRecherche(Long idCategorie, String recherche) {
		return articleDAO.consulterParCategorieEtNom(idCategorie, recherche);
	}

	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void ajouterEnchere(Enchere enchere) throws BusinessException {

		BusinessException be = new BusinessException();

		boolean isValid = hasUtilisateur(enchere.getUtilisateur().getId(), be);
		isValid &= hasArticle(enchere.getArticle().getId(), be);
		isValid &= isCreditSuffisant(enchere.getMontantEnchere(), enchere.getUtilisateur().getId(), be);
		isValid &= isEnchereOuverte(enchere.getArticle().getId(), be);
		isValid &= isOffreSuperieureDerniereEnchere(enchere.getMontantEnchere(), enchere.getArticle().getId(), be);
		isValid &= isOffreSuperieureDerniereEnchere(enchere.getMontantEnchere(), enchere.getArticle().getId(), be);
		

		if(isValid) {
			try {
				if(hasAutreEnchere(enchere.getArticle().getId())) {
					this.ajouterCredits(this.recupererDerniereEnchere(enchere.getArticle().getId()).getMontantEnchere(),this.recupererDerniereEnchere(enchere.getArticle().getId()).getUtilisateur().getId(), be);
				}
				this.retirerCredits(enchere.getMontantEnchere(), enchere.getUtilisateur().getId(), be);
				this.enchereDAO.ajouterEnchere(enchere);
				
			} catch (Exception e) {
				be.add("Erreur d'accès à la base de données");
				logger.error(e);
				throw be;
			}
		}else {
			throw be;
		}
	}

    @Override
    public void ajouterCredits(int nbAjout, long idUtilisateur,  BusinessException be) {
        if (nbAjout <= 0) {
            throw new IllegalArgumentException ("Nombre de crédits à ajouter doit être positif");
        }
        utilisateurDAO.ajouterCredits(nbAjout, idUtilisateur);
    }
    
	@Override
	public void retirerCredits(int nbRetire, long idUtilisateur,  BusinessException be) {
		if (nbRetire <= 0) {
			throw new IllegalArgumentException("Nombre de crédits à retirer doit être positif");
		}
		Utilisateur utilisateur = utilisateurDAO.consulterParId(idUtilisateur);
		if (utilisateur.getCredit() < nbRetire) {
			throw new IllegalStateException(
					"Crédits insuffisants : solde actuel = " + utilisateur.getCredit() + ", demandé = " + nbRetire);
		}
		utilisateurDAO.retirerCredits(nbRetire, idUtilisateur);
	}

	
	@Override
	public boolean hasArticle(long idArticle, BusinessException be) {
		if(this.articleDAO.hasArticle(idArticle)) {
			return true;
		}else {
			be.add("L'article est introuvable");
			return false;
		}
	}

	@Override
	public boolean hasUtilisateur(long idUtilisateur, BusinessException be) {
		if(this.utilisateurDAO.hasUtilisateur(idUtilisateur)) {
			return true;
		}else {
			be.add("L'enchérisseur.euse n'existe plus (qui êtes vous ?)");
			return false;
		}
	}

	@Override
	public List<Article> consulterArticlesParQuerySQLPersonnalisee(String sql) {
		return this.articleDAO.sqlQueryPersonnalisee(sql);
	}

	@Override
	public boolean aEncheri(long idArticle, long idUtilisateur) {
		return enchereDAO.hasEnchereUtilisateur(idUtilisateur, idArticle);
	}
	
	public boolean isCreditSuffisant(int montant, long idUtilisateur, BusinessException be) {
		if (montant <= 0) {
			be.add("Le montant doit être supérieur à 0");
			return false;
		}
		if (montant > this.utilisateurDAO.consulterCredit(idUtilisateur)) {
			be.add("le montant "+ montant+ " est supérieur à la solde du compte ("+this.utilisateurDAO.consulterCredit(idUtilisateur)+")");
			return false;
		}else {
			return true;
		}
	}

	@Override
	public boolean isOffreSuperieureDerniereEnchere(int montant, long idArticle, BusinessException be) {

		if (hasAutreEnchere(idArticle)){
			if(montant <= this.recupererDerniereEnchere(idArticle).getMontantEnchere()) {
				be.add("le montant foit être supérieur ou égal à la dernière enchère ("+this.recupererDerniereEnchere(idArticle).getMontantEnchere()+")");
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	
	@Override
	public boolean isOffreSupérieureMAP(int montant, long idArticle, BusinessException be) {
		if (montant < this.articleDAO.consulterParId(idArticle).getMiseAPrix()) {
			be.add("le montant foit être supérieur ou égal à la mise à prix ("+this.articleDAO.consulterParId(idArticle).getMiseAPrix()+")");
			return false;
		}else {
			return true;
		}
		
	}
	
	
	@Override
	public boolean isEnchereOuverte(long idArticle, BusinessException be) {
		if(articleDAO.isArticleEtatOuvert(idArticle)) {
			return true;
		}else {
			be.add("L'article n'est plus en vente");
			return false;
		}
	}

	@Override
	public boolean hasAutreEnchere(long idArticle) {
		return this.recupererDerniereEnchere(idArticle)!=null;
	}

	@Override
	public void mettreAJourArticle(Article article) {
		articleDAO.mettreAJourArticle(article);
	}

	public boolean verifierProprietaireArticle(Long idArticle, Long idUtilisateur) {
		Article article = consulterArticleParId(idArticle);
		return article != null && Objects.equals(article.getUtilisateur().getId(), idUtilisateur);
	}

	@Override
	public boolean existeArticle(long idArticle) {
		return articleDAO.existeArticle(idArticle);
	}

}
