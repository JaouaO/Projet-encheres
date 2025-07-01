package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class UtilisateurController {

	@GetMapping({ "/", "/accueil" })
	public String afficherAccueil(Model model) {
		return "index";
	}

	@GetMapping("/connexion")
	public String afficherConnexion(Model model) {
		return "connexion";
	}
	
	//pas sur si il faut remttre post /connexion comme c'est la même page ou pas
		@PostMapping("/connexion/modifier")
		public String connexionModifier(Model model) {
			// TODO: process POST request

			return "profil-utilisateur";
		}

	//pas sur si il faut remttre post /connexion comme c'est la même page ou pas
	@PostMapping("/connexion/mon_profil")
	public String connexion(Model model) {
		// TODO: process POST request

		return "creer-profil";
	}
	
	@GetMapping("/inscription")
	public String afficherCreationCompte(Model model) {
		//TODO: process PUT request
		
		return "creer-compte";
	}
	
	
	//pas sur, peut-être qu'il faut que ce soir /connexion/modifier pour correspondre aux routes ?
	@PostMapping("/inscription")
	public String creerCompte( Model model ) {
		//TODO: process POST request
		
		return "creer-profil";
	}
	
	@PostMapping("/creer-profil")
	public String creerProfil( Model model) {
		return "portail-encheres";
	}
	

}
