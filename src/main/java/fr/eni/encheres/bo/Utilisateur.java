package fr.eni.encheres.bo;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

	private long id;
	private String pseudo;
	private String prenom;
	private String nom;
	private String email;
	private String motDePasse;
	private String telephone;
	private String rue;
	private String codePostal;
	private String ville;
	private int credit = 100;
	private boolean admin;

	private List<Article> articles;
	private List<Enchere> encheres;
	
	
	

	public Utilisateur() {
		
		this.articles = new ArrayList<Article>();
		this.encheres = new ArrayList<Enchere>();
	}
	
	
	public Utilisateur(long id, String pseudo,String prenom, String nom, String email, String motDePasse, String telephone, String rue,
			String codePostal, String ville, int credit, boolean admin) {
		this.id = id;
		this.pseudo = pseudo;
		this.prenom = prenom;
		this.nom = nom;
		this.email = email;
		this.motDePasse = motDePasse;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.credit = credit;
		this.admin = admin;
		this.articles = new ArrayList<Article>();
		this.encheres = new ArrayList<Enchere>();
	}
	
	public void addArticle(Article article) {
		this.articles.add(article);
	}
	
	public void addEnchere(Enchere enchere) {
		this.encheres.add(enchere);
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public Iterable<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	public Iterable<Enchere> getEncheres() {
		return encheres;
	}
	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Utilisateur [id=");
		builder.append(id);
		builder.append(", pseudo=");
		builder.append(pseudo);
		builder.append(", prenom=");
		builder.append(prenom);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", email=");
		builder.append(email);
		builder.append(", motDePasse=");
		builder.append(motDePasse);
		builder.append(", telephone=");
		builder.append(telephone);
		builder.append(", rue=");
		builder.append(rue);
		builder.append(", codePostal=");
		builder.append(codePostal);
		builder.append(", ville=");
		builder.append(ville);
		builder.append(", credit=");
		builder.append(credit);
		builder.append(", admin=");
		builder.append(admin);
		builder.append(", articles=");
		builder.append(articles);
		builder.append(", encheres=");
		builder.append(encheres);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}
