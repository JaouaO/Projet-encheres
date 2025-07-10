package fr.eni.encheres.controller;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Article;

import java.util.List;

import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("utilisateurEnSession")
@Controller
public class UtilisateurController {

	private EnchereService enchereService;

	private UtilisateurService utilisateurService;

	public UtilisateurController(EnchereService enchereService, UtilisateurService utilisateurService) {
		this.enchereService = enchereService;
		this.utilisateurService = utilisateurService;

	}

	@GetMapping({ "/", "/accueil" })
	public String afficherAccueil(
			@RequestParam(name = "idCategorie", required = false, defaultValue = "0") Long idCategorie,
			@RequestParam(name = "text", required = false, defaultValue = "") String text, HttpSession session, Model model) {

		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

		if(utilisateurSession!=null) {
			return  "redirect:/portail-encheres";
		}
		
		List<Article> articles;

		if (idCategorie != 0 && !text.isBlank()) {
			articles = enchereService.consulterParCategorieEtRecherche(idCategorie, text);
		} else if (!text.isBlank()) {
			articles = enchereService.consulterParRecherche(text);
		} else if (idCategorie != 0) {
			articles = enchereService.consulterParCategorie(idCategorie);
		} else {
			articles = enchereService.consulterToutArticle();

		}

		model.addAttribute("articles", articles);
		model.addAttribute("categories", enchereService.consulterToutCategorie());
		model.addAttribute("idCategorie", idCategorie);
		model.addAttribute("text", text);

		return "index";
	}

	@GetMapping("/connexion")
	public String afficherConnexion(Model model) {
		return "connexion";
	}


	@PostMapping("/connexion")
	public String connecterUtilisateur(@RequestParam("pseudo") String pseudo,

			@RequestParam("motDePasse") String motDePasse, Model model, HttpSession session) {

		Utilisateur utilisateur = utilisateurService.verifierConnexion(pseudo, motDePasse);

		if (utilisateur != null) {
			session.setAttribute("utilisateurSession", utilisateur);
			return "redirect:/portail-encheres";
		} else {
			model.addAttribute("erreur", "Pseudo ou mot de passe incorrect.");
			return "connexion";
		}
	}

	@GetMapping("/portail-encheres")
	public String afficherPortail(HttpSession session,
			@RequestParam(name = "radio", required = false, defaultValue = "achats") String radio,
			@RequestParam(name = "checkbox", required = false, defaultValue = "encheresOuvertes") String[] checkbox,
			@RequestParam(name = "idCategorie", required = false, defaultValue = "0") Long idCategorie,
			@RequestParam(name = "text", required = false, defaultValue = "") String text, Model model) {

		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		if (utilisateur == null) {
			return "redirect:/connexion";
		}

		long idUtilisateurSession = utilisateur.getId();


		String sqlQuery = """
				SELECT a.id, a.nom, a.description, a.date_debut, a.date_fin,
				             a.mise_a_prix, a.prix_vente, a.etat_vente, a.id_vendeur,
					         a.id_categorie, a.chemin_img,
					         u.pseudo, u.rue, u.code_postal, u.ville
					FROM article a
					INNER JOIN utilisateur u ON a.id_vendeur = u.id
					WHERE a.id_vendeur
					""";


		String sqlQueryRemportees = """
				SELECT a.id, a.nom, a.description, a.date_debut, a.date_fin,
					             a.mise_a_prix, a.prix_vente, a.etat_vente, a.id_vendeur,
						         a.id_categorie, a.chemin_img,
						         u.pseudo, u.rue, u.code_postal, u.ville
					      FROM article a
					      INNER JOIN utilisateur u ON a.id_vendeur = u.id
						  WHERE (a.etat_vente = 'terminee' OR a.etat_vente = 'livree')
						  AND a.id_vendeur!=
					""" + utilisateur.getId() + " ";

		String idVendeurString = "";
		String etatString = "";
		String categorieString = "";
		String rechercheString = "";
		boolean remportees = false;
		boolean aEncheri = false;
		boolean mainQuery = false;

		if (radio.equals("achats")) {
			idVendeurString += "!=" + utilisateur.getId() + " ";
			for (int i = 0; i < checkbox.length; i++) {

				if (checkbox[i].equals("encheresOuvertes")) {
					mainQuery = true;
					if (etatString.length() > 0) {
						etatString += "OR etat_vente = 'en_cours' ";
					} else {
						etatString += "AND ( etat_vente = 'en_cours' ";
					}
				}
				if (checkbox[i].equals("encheresEnCours")) {
					aEncheri = true;
				}
				if (checkbox[i].equals("encheresRemportées")) {
					remportees = true;
				}
			}
			if (etatString.length() > 0) {
				etatString += ") ";
			}
		} else {
			idVendeurString += "=" + utilisateur.getId() + " ";
			for (int i = 0; i < checkbox.length; i++) {

				if (checkbox[i].equals("ventesEnCours")) {
					mainQuery = true;
					if (etatString.length() > 0) {
						etatString += "OR etat_vente = 'en_cours' ";
					} else {
						etatString += "AND ( etat_vente = 'en_cours' ";
					}

				}
				if (checkbox[i].equals("ventesNonDebutees")) {
					mainQuery = true;
					if (etatString.length() > 0) {
						etatString += "OR etat_vente = 'non_debutee' ";
					} else {
						etatString += "AND ( etat_vente = 'non_debutee' ";
					}

				}
				if (checkbox[i].equals("ventesTerminees")) {
					mainQuery = true;
					if (etatString.length() > 0) {
						etatString += "OR etat_vente = 'terminee' OR etat_vente = 'livree' ";
					} else {
						etatString += "AND ( etat_vente = 'terminee' OR etat_vente = 'livree' ";
					}

				}
			}
			if (etatString.length() > 0) {
				etatString += ") ";
			}

		}

		if (idCategorie != 0) {
			categorieString += "AND a.id_categorie =" + idCategorie + " ";
		}
		if (!text.isBlank()) {
			rechercheString += "AND LOWER(a.nom) LIKE LOWER('%" + text + "%') ";
		}

		String mainSqlQuery = sqlQuery + idVendeurString + etatString + categorieString + rechercheString;

		List<Article> articlesAAfficher = new ArrayList<Article>();

		if (mainQuery) {
			articlesAAfficher = enchereService.consulterArticlesParQuerySQLPersonnalisee(mainSqlQuery);

		}

		if (aEncheri) {
			String aEncheriSqlQuery = sqlQuery + idVendeurString + "AND ( etat_vente = 'en_cours') " + categorieString
					+ rechercheString;
			List<Article> articlesChercherEnchereArticles = enchereService
					.consulterArticlesParQuerySQLPersonnalisee(aEncheriSqlQuery);
			for (Article article : articlesChercherEnchereArticles) {
				long idArticle = article.getId();
				if (enchereService.aEncheri(idArticle, idUtilisateurSession)) {
					boolean addArticle = true;
					for (Article article2 : articlesAAfficher) {
						if (article.getId() == article2.getId()) {
							addArticle = false;
						}
					}
					if (addArticle) {
						articlesAAfficher.add(article);
					}
				}
			}
		}

		if (remportees) {
			sqlQueryRemportees += categorieString + rechercheString;
			List<Article> articlesEtatTermine = enchereService
					.consulterArticlesParQuerySQLPersonnalisee(sqlQueryRemportees);
			for (Article article : articlesEtatTermine) {

				long idArticle = article.getId();
				Enchere derniereEnchere = enchereService.recupererDerniereEnchere(idArticle);
				if (derniereEnchere != null) {
					long IdDernierEncherisseur = derniereEnchere.getUtilisateur().getId();
					if (IdDernierEncherisseur == idUtilisateurSession) {
						boolean addArticle = true;
						for (Article article2 : articlesAAfficher) {
							if (article.getId() == article2.getId()) {
								addArticle = false;
							}
						}
						if (addArticle) {
							articlesAAfficher.add(article);
						}
					}

				}

			}

		}

		model.addAttribute("articlesAAfficher", articlesAAfficher);
		model.addAttribute("utilisateur", utilisateur);
		model.addAttribute("categories", enchereService.consulterToutCategorie());
		model.addAttribute("idCategorie", idCategorie);
		model.addAttribute("text", text);

		return "portail-encheres";

	}

	@GetMapping("/profil")
	public String afficherProfil(@RequestParam(name = "id", required = false, defaultValue = "-1") Long IdUtilisateur,
			HttpSession session, Model model) {
		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

		if (IdUtilisateur == -1 || IdUtilisateur == utilisateurSession.getId()) {
			Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
			model.addAttribute("utilisateur", utilisateur);
			model.addAttribute("afficherModifier", true);
		} else {
			Utilisateur utilisateur = utilisateurService.consulterParId(IdUtilisateur);
			model.addAttribute("utilisateur", utilisateur);
			model.addAttribute("afficherModifier", false);
		}

		return "profil";
	}

	@GetMapping("/inscription")
	public String afficherCreerCompte(Model model) {
		Utilisateur utilisateur = new Utilisateur();
		model.addAttribute("utilisateur", utilisateur);

		return "creer-compte";
	}

	@PostMapping("/inscription")
	public String creerCompte(@ModelAttribute("utilisateur") Utilisateur utilisateur, Model model) {
		try {
			utilisateurService.creerUtilisateur(utilisateur);
			return "redirect:/portail-encheres";
		} catch (IllegalArgumentException e) {
			model.addAttribute("erreur", e.getMessage());
			return "creer-compte";
		}
	}

	@GetMapping("/profil/modifier")
	public String afficherModifierProfil(Model model, HttpSession session) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		model.addAttribute("utilisateur", utilisateur);
		return "modifier-profil";
	}

	@PostMapping("/profil/modifier")
	public String modifierProfil(@ModelAttribute("utilisateur") Utilisateur utilisateurModifie, HttpSession session,
			Model model) {
    
		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

		utilisateurSession.setPseudo(utilisateurModifie.getPseudo());
		utilisateurSession.setNom(utilisateurModifie.getNom());
		utilisateurSession.setPrenom(utilisateurModifie.getPrenom());
		utilisateurSession.setEmail(utilisateurModifie.getEmail());
		utilisateurSession.setTelephone(utilisateurModifie.getTelephone());
		utilisateurSession.setRue(utilisateurModifie.getRue());
		utilisateurSession.setCodePostal(utilisateurModifie.getCodePostal());
		utilisateurSession.setVille(utilisateurModifie.getVille());
		utilisateurSession.setMotDePasse(utilisateurModifie.getMotDePasse());

		try {

			utilisateurService.modifierUtilisateur(utilisateurSession.getId(), utilisateurSession);
			session.setAttribute("utilisateurSession", utilisateurSession);
			model.addAttribute("message", "Profil mis à jour avec succès !");
		} catch (IllegalArgumentException e) {
			model.addAttribute("erreur", e.getMessage());
		}

		model.addAttribute("utilisateur", utilisateurSession);
		model.addAttribute("afficherModifier", true);

		return "profil";
	}


	@PostMapping("/profil/supprimer")
	public String supprimerCompte(HttpSession session, SessionStatus sessionStatus) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");

		if (utilisateur != null) {
			utilisateurService.supprimerUtilisateur(utilisateur.getId());
			session.invalidate();
			sessionStatus.setComplete();

		}

		return "redirect:/accueil";
	}



	@GetMapping("/session-cloture")
	public String finSession(SessionStatus sessionStatus, HttpSession session) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");

		if (utilisateur != null) {
			session.setAttribute("utilisateurSession", null);
		}
		sessionStatus.setComplete();

		return "redirect:/accueil";
	}

	@ModelAttribute("membreEnSession")
	public Utilisateur addUtilisateurEnSession() {

		return new Utilisateur();
	}

//	@PostMapping("/portail-encheres")
//	public String filtrerArticles(@RequestParam("type") String type,
//			@RequestParam(value = "categorie", required = false) String categorie, Model model) {
//
//		List<Article> articles;
//
//		// Récupération des articles selon le type (achat ou vente)
//		if ("vente".equals(type)) {
//			articles = enchereService.consulterParEtat("Mes ventes");
//		} else {
//			articles = enchereService.consulterParEtat("Enchère ouverte");
//		}
//
//		// Si une catégorie est sélectionnée, on filtre avec une boucle
//		if (categorie != null && !categorie.isEmpty()) {
//			List<Article> articlesFiltres = new ArrayList<>();
//
//			for (Article article : articles) {
//				if (article.getCategorie().getLibelle().equalsIgnoreCase(categorie)) {
//					articlesFiltres.add(article);
//				}
//			}
//		}
//		// articles = articlesFiltres; // problème à regler
//
//		// On prépare les données pour l'affichage dans la vue
//		model.addAttribute("articles", articles);
//		model.addAttribute("categories", enchereService.consulterToutCategorie());
//		model.addAttribute("type", type);
//
//		return "portail-encheres";
}
