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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

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
		Utilisateur utilisateur = new Utilisateur();
		model.addAttribute("utilisateur", utilisateur);

		return "creer-compte";
	}

	@PostMapping("/inscription")
	public String creerCompte(Model model) {
		// TODO: process PUT request

		return "index";
	}

	
	@GetMapping("/profil/modifier")
	public String afficherModifierProfil(Model model) {
		// TODO: process POST request

		return "modifier-profil";
	}


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


	@GetMapping({ "/rechercher" })
	public String afficherPortail (Model model) {

		List<Article> articles = enchereService.consulterParEtat("Enchère ouverte");

		model.addAttribute("articles", articles);

		List<Categorie> categories = this.enchereService.consulterToutCategorie();

		model.addAttribute("categories", categories);

			return "portail-encheres";
		}



}
