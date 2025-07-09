package fr.eni.encheres.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Categorie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String libelle;
	
	List<Article> articles;
	
	
	

	public Categorie() {
		this.articles = new ArrayList<Article>();
	}

	public Categorie(long id, String libelle) {
		this.id = id;
		this.libelle = libelle;
		this.articles = new ArrayList<Article>();
	}

	
	public void addArticle(Article article) {
		this.articles.add(article);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Iterable<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Categorie [id=");
		builder.append(id);
		builder.append(", libelle=");
		builder.append(libelle);
		builder.append("]");
		return builder.toString();
	}

	
	
}
