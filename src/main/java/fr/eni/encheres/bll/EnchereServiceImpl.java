package fr.eni.encheres.bll;

import java.util.Comparator;
import java.util.List;
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
	public void mettreEnVente(long idArticle) {
		this.articleDAO.mettreEnVente(idArticle);
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
	public List<Article> consulterParCategorieEtRecherche(Long idCategorie, String recherche) {
		return articleDAO.consulterParCategorieEtNom(idCategorie, recherche);
	}

	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void ajouterEnchere(Enchere enchere) throws BusinessException {

		BusinessException be = new BusinessException();

		boolean isValid = hasUtilisateur(enchere.getUtilisateur().getId(), be);
		isValid &= hasArticle(enchere.getArticle().getId(), be);
		isValid &= isCreditSuffisant(enchere.getMontantEnchere(), enchere.getUtilisateur(), be);
		isValid &= isEnchereOuverte(enchere.getArticle().getId(), be);
		isValid &= isOffreSupérieure(enchere.getMontantEnchere(), enchere.getArticle().getId(), be);

		if(isValid) {
			try {
				this.retirerCredits(enchere.getMontantEnchere(), enchere.getUtilisateur().getId());
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
	public void retirerCredits(int nbRetire, long idUtilisateur) {
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
		if (article == null) {
			model.addAttribute("erreur", "Article non trouvé");
			System.out.println("Article non trouvé");
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
	public boolean isCreditSuffisant(int montant, Utilisateur utilisateur, BusinessException be) {
		if (montant <= 0) {
			model.addAttribute("erreur", "Le montant doit être supérieur à zéro.");
			System.out.println("montant doit être supérieur à zéro");

		}
		if (montant > CreditUserEnSession) {
			model.addAttribute("erreur", "Vous n'avez pas assez de crédits pour effectuer cette enchère.");
			System.out.println("Vous n'avez pas assez de crédits pour effectuer cette enchère.");

		}
	}

	@Override
	public boolean isOffreSupérieure(int montant, long idArticle, BusinessException be) {
		if (montant <= article.getMiseAPrix()) {
			model.addAttribute("erreur", "Votre enchère doit être supérieure à la mise à prix.");
			System.out.println("Votre enchère doit être supérieure à la mise à prix.");
		}
		if (derniereEnchere != null && montant <= derniereEnchere.getMontantEnchere()) {
			model.addAttribute("erreur", "Votre enchère doit être supérieure à la meilleure offre actuelle.");
			System.out.println("Votre enchère doit être supérieure à la meilleure offre actuelle.");

		}
	}

	@Override
	public boolean isEnchereOuverte(long idArticle, BusinessException be) {
		// TODO Auto-generated method stub
		return false;
	}

}
