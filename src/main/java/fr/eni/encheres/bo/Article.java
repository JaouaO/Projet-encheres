package fr.eni.encheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Article implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String nom;
	private String description;
	private LocalDateTime dateDebutEnchere;
	private LocalDateTime dateFinEnchere;
	private int miseAPrix;
	private int prixVente;
	private String etatVente;

	private Retrait lieuRetrait;
	private Utilisateur utilisateur;
	private Categorie categorie;
	private List<Enchere> encheres;
	private String cheminImg;


	public Article() {
		this.encheres = new ArrayList<Enchere>();
		this.etatVente = "nonDebutee";
	}



	public Article(long id, String nom, String description, LocalDateTime dateDebutEnchere,
				   LocalDateTime dateFinEnchere, int miseAPrix, int prixVente, String etatVente, Retrait lieuRetrait,
				   Utilisateur utilisateur, Categorie categorie, String cheminImg) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.dateDebutEnchere = dateDebutEnchere;
		this.dateFinEnchere = dateFinEnchere;
		this.miseAPrix = miseAPrix;
		this.prixVente = prixVente;
		this.etatVente = etatVente;
		this.lieuRetrait = lieuRetrait;
		this.utilisateur = utilisateur;
		this.categorie = categorie;
		this.cheminImg = cheminImg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateDebutEnchere() {
		return dateDebutEnchere;
	}

	public void setDateDebutEnchere(LocalDateTime dateDebutEnchere) {
		this.dateDebutEnchere = dateDebutEnchere;
	}

	public LocalDateTime getDateFinEnchere() {
		return dateFinEnchere;
	}

	public void setDateFinEnchere(LocalDateTime dateFinEnchere) {
		this.dateFinEnchere = dateFinEnchere;
	}

	public int getMiseAPrix() {
		return miseAPrix;
	}

	public void setMiseAPrix(int miseAPrix) {
		this.miseAPrix = miseAPrix;
	}

	public int getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(int prixVente) {
		this.prixVente = prixVente;
	}

	public String getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(String etatVente) {
		this.etatVente = etatVente;
	}

	public Retrait getLieuRetrait() {
		return lieuRetrait;
	}

	public void setLieuRetrait(Retrait lieuRetrait) {
		this.lieuRetrait = lieuRetrait;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Categorie getCategorie() {
		return this.categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public List<Enchere> getEncheres() {
		return encheres;
	}

	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}

	public String getCheminImg() {
		return cheminImg;
	}

	public void setCheminImg(String cheminImg) {
		this.cheminImg = cheminImg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Article [id=");
		builder.append(id);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", description=");
		builder.append(description);
		builder.append(", dateDebutEnchere=");
		builder.append(dateDebutEnchere);
		builder.append(", dateFinEnchere=");
		builder.append(dateFinEnchere);
		builder.append(", miseAPrix=");
		builder.append(miseAPrix);
		builder.append(", prixVente=");
		builder.append(prixVente);
		builder.append(", etatVente=");
		builder.append(etatVente);
		builder.append(", lieuRetrait=");
		builder.append(lieuRetrait);
		builder.append(", utilisateur=");
		builder.append(utilisateur);
		builder.append(", categorie=");
		builder.append(categorie);
		builder.append(", encheres=");
		builder.append(encheres);
		builder.append("]");
		builder.append(", cheminImg=");
		builder.append(cheminImg);
		return builder.toString();
	}


}
