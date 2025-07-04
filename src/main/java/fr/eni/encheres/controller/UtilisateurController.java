package fr.eni.encheres.controller;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Article;

import fr.eni.encheres.bo.Categorie;

import java.util.List;


import fr.eni.encheres.bo.Utilisateur;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;


import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.SessionAttributes;


import org.springframework.web.bind.support.SessionStatus;
import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;


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
	public String afficherAccueil(Model model) {

		List<Article> articles = enchereService.consulterToutArticle();
		model.addAttribute("articles", articles);

		List<Categorie> categories = this.enchereService.consulterToutCategorie();


		model.addAttribute("categories", categories);


		return "index";
		// if(utilisateurEnSession != null){
		// return "portail-encheres";
		// }
	}
	
	@PostMapping("/connexion")
	public String connecterUtilisateur(@RequestParam("pseudo") String pseudo,
            @RequestParam("motDePasse") String motDePasse,
            Model model,
            HttpSession session) {
 
	    Utilisateur utilisateur = utilisateurService.verifierConnexion(pseudo, motDePasse);
 
	    if (utilisateur != null) {
	        session.setAttribute("utilisateurSession", utilisateur);
	        System.out.println("Test");
	        return "redirect:/accueil";
	    } else {
	        model.addAttribute("erreur", "Pseudo ou mot de passe incorrect.");
	        System.out.println("test2");
	        return "connexion";
	    }
	}
	

	@GetMapping("/connexion")
	public String afficherConnexion(Model model) {
		return "connexion";
	}

	@PostMapping("/connexion")
	public String connecterUtilisateur(@RequestParam("pseudo") String pseudo,
									   @RequestParam("motDePasse") String motDePasse,
									   Model model,
									   HttpSession session) {

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
	public String afficherPortail(HttpSession session, Model model) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		if (utilisateur == null) {
			return "redirect:/connexion";
		}

		List<Article> articles = enchereService.consulterToutArticle();
		List<Categorie> categories = enchereService.consulterToutCategorie();

		model.addAttribute("articles", articles);
		model.addAttribute("categories", categories);
		model.addAttribute("utilisateur", utilisateur);

		return "portail-encheres";
	}

	@GetMapping("/profil")
	public String afficherProfil(Model model) {
		return "profil";
	}

	@GetMapping("/inscription")
	public String afficherCreerCompte(Model model) {
		// TODO: process PUT request

		return "creer-compte";
	}

	@PostMapping("/inscription")
	public String creerCompte(Model model) {
		// TODO: process PUT request

		return "index";
	}

	// pas sur, peut-être qu'il faut que ce soir /connexion/modifier pour
	// correspondre aux routes ?
	@GetMapping("/profil/modifier")
	public String afficherModifierProfil(Model model) {
		// TODO: process POST request

		return "modifier-profil";
	}

	// pas sur si il faut remttre post /connexion comme c'est la même page ou pas
	@PostMapping("/profil/modifier")
	public String modifierProfil(Model model) {
		// TODO: process POST request

		return "profil";
	}

	@GetMapping("/session-cloture")
	public String finSession(SessionStatus sessionStatus) {

		sessionStatus.setComplete();

		return "redirect:/accueil";
	}
	
	@ModelAttribute("membreEnSession")
	public Utilisateur addUtilisateurEnSession() {
		System.out.println("Add membre en session");
		return new Utilisateur();
	}


	@GetMapping({ "/portail-encheres" })
	public String afficherPortail (Model model) {

		List<Article> articles = enchereService.consulterParEtat("Enchère ouverte");

		model.addAttribute("articles", articles);

		List<Categorie> categories = this.enchereService.consulterToutCategorie();

		model.addAttribute("categories", categories);

			return "portail-encheres";
		}

    @PostMapping("/portail-encheres")
    public String filtrerArticles(
            @RequestParam("type") String type,
            @RequestParam(value = "categorie", required = false) String categorie,
            Model model) {

		List<Article> articles;

		// Récupération des articles selon le type (achat ou vente)
		if ("vente".equals(type)) {
			articles = enchereService.consulterParEtat("Mes ventes");
		} else {
			articles = enchereService.consulterParEtat("Enchère ouverte");
		}

		// Si une catégorie est sélectionnée, on filtre avec une boucle
		if (categorie != null && !categorie.isEmpty()) {
			List<Article> articlesFiltres = new ArrayList<>();

			for (Article article : articles) {
				if (article.getCategorie().getLibelle().equalsIgnoreCase(categorie)) {
					articlesFiltres.add(article);
				}
			}
}
			articles = articlesFiltres;  // problème à regler

			// On prépare les données pour l'affichage dans la vue
			model.addAttribute("articles", articles);
			model.addAttribute("categories", enchereService.consulterToutCategorie());
			model.addAttribute("type", type);

			return "portail-encheres";
		}




}
