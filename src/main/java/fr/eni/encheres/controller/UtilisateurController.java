package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;




@Controller
public class UtilisateurController {

	@GetMapping({ "/", "/accueil" })
	public String afficherAccueil(Model model) {
		return "index";
		//if(utilisateurEnSession != null){
	//return "portail-encheres";
	//}
	}
	

	@GetMapping("/connexion")
	public String afficherConnexion(Model model) {
		return "connexion";
	}
	

	//pas sur si il faut remttre post /connexion comme c'est la même page ou pas
	@PostMapping("/profil")
	public String connexion(Model model) {
		// TODO: process POST request

		return "index";
	}
	
	@GetMapping("/inscription")
	public String afficherCreerCompte(Model model) {
		//TODO: process PUT request
		
		return "creer-compte";
	}
	
	@PostMapping("/inscription")
	public String creerCompte(Model model) {
		//TODO: process PUT request
		
		return "modifier-profil";
	}
	
	
	
	//pas sur, peut-être qu'il faut que ce soir /connexion/modifier pour correspondre aux routes ?
	@GetMapping("/connexion/modifier")
	public String afficherModifierProfil( Model model ) {
		//TODO: process POST request
		
		return "modifier-profil";
	}
	
	
	//pas sur si il faut remttre post /connexion comme c'est la même page ou pas
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
	

}
