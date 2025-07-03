package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Utilisateur;

@SessionAttributes("utilisateurEnSession")
@Controller
public class EnchereController {

    private EnchereService enchereservice;
    private UtilisateurService utilisateurService;



public EnchereController(EnchereService enchereservice, UtilisateurService utilisateurService) {
		this.enchereservice = enchereservice;
		this.utilisateurService = utilisateurService;
	}

//pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
	@GetMapping("/ventes/details")
	public String afficherDetailsVentes(@RequestParam(name = "id") long idArticle, Model model) {
		// check l'utilisateur pour voir si c'est achat ou vente
		return "ventes-details";
	}

//pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
	@GetMapping("/achats/details")
//@RequestParam(name = "id") long idArticle,
	public String afficherDetailsAchats(Model model) {
		// check l'utilisateur pour voir si c'est achat ou vente
		return "achats-details";
	}

@GetMapping("/vente")
public String afficherVente( Model model) {
	model.addAttribute("categories", this.enchereservice.consulterToutCategorie());
	Article article = new Article();
	model.addAttribute("article", article);

	
    return "creer-nouvelle-vente";
}

	@PostMapping("/creer-nouvelle-vente")
	public String getMethodName(@ModelAttribute Article article, Model model) {
		Utilisateur utilisateur = utilisateurService.consulterParId(1);
		article.setUtilisateur(utilisateur);
		article.setEtatVente("NON_DEBUTEE");
		article.setLieuRetrait(utilisateur.getRetrait());
		System.out.println(article);
		
		enchereservice.creerArticle(article);
		

		return "redirect:/accueil";
	}

//doit request aussi l'ID de l'article
	@PostMapping("/encherir")
//@RequestParam(name=nbPoints)
	public String encherir(int nbPoints, Model model) {
		// TODO
		return "achats-details";// + idArticle;
	}

//doit request aussi l'ID de l'article
	@PostMapping("/retire")
	public String retire(Model model) {
		// TODO checker que le vendeur ET l'acheteur l'ont marqué comme retiré
		return "redirect:/achats/details";// + idArticle;
	}

}
