package fr.eni.encheres.bo;

import java.util.List;

public class Utilisateur {

	private long id;
	private String prenom;
	private String email;
	private String motDePasse;
	private String telephone;
	private String rue;
	private String codePostal;
	private String ville;
	private int credit;
	private boolean admin;
	
	
	

	private List<Article> articles;
	private List<Enchere> encheres;

}
