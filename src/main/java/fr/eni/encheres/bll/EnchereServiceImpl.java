package fr.eni.encheres.bll;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.UtilisateurDAO;

@Service
public class EnchereServiceImpl implements EnchereService {

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
	public void ajouterEnchere(Enchere enchere) {
		this.enchereDAO.ajouterEnchere(enchere);

	}

	@Override
	public List<Article> consulterParCategorieEtRecherche(Long idCategorie, String recherche) {
		return articleDAO.consulterParCategorieEtNom(idCategorie, recherche);
	}


}
