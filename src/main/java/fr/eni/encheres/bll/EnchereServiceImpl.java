package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.EnchereDAO;

@Service
public class EnchereServiceImpl implements EnchereService {

	private ArticleDAO articleDAO;
	private EnchereDAO enchereDAO;



	public EnchereServiceImpl(ArticleDAO articleDAO, EnchereDAO enchereDAO) {
		this.articleDAO = articleDAO;
		this.enchereDAO = enchereDAO;
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
	public Article consulterParId(long idArticle) {

		return this.articleDAO.consulterParId(idArticle);
	}

	@Override
	public List<Article> consulterTout() {
		return this.articleDAO.consulterTout();
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

}
