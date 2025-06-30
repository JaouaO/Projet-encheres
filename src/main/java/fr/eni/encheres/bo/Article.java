package fr.eni.encheres.bo;

import java.time.LocalDateTime;
import java.util.List;

public class Article {

	private long id;
	private String nom;
	private String description;
	private LocalDateTime dateDebutEnchere;
	private LocalDateTime dateFinEnchere;
	private int miseAPrix;
	private int prixVente;
	private int etatVente;
	
	private Retrait lieuRetrait;
	private Utilisateur utilisateur;
	private Categorie categorie;
	private List<Enchere> encheres;
	
	
}
