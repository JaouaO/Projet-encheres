package fr.eni.encheres.bo;

import java.util.List;

public class Categorie {

	private long id;
	private String libelle;
	
	List<Article> articles;

	public Categorie(long id, String libelle, List<Article> articles) {
		this.id = id;
		this.libelle = libelle;
		this.articles = articles;
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

	
	
}
