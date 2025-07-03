package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


//@SessionAttributes("utilisateurEnSession")
@Controller
public class EnchereController {

//	EnchereService enchereservice;
//
//	public EnchereController(EnchereService enchereservice) {
//		this.enchereservice = enchereservice;
//	}
//	
	
//pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
@GetMapping("/ventes/details")
public String afficherDetailsVentes(@RequestParam(name = "id") long idArticle, Model model) {
	//check l'utilisateur pour voir si c'est achat ou vente
    return "ventes-details";
}

//pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
@GetMapping("/achats/details")
//@RequestParam(name = "id") long idArticle,
public String afficherDetailsAchats( Model model) {
	//check l'utilisateur pour voir si c'est achat ou vente
  return "achats-details";
}

@GetMapping("/vente")
public String afficherVente( Model model) {
	//model.addAttribute("categories", model)
	
    return "creer-nouvelle-vente";
}

@PostMapping("/creer-nouvelle-vente")
public String getMethodName( Model model) {
	
	return "index";
}


//doit request aussi l'ID de l'article
@PostMapping("/encherir")
//@RequestParam(name=nbPoints)
public String encherir( int nbPoints, Model model) {
	//TODO
	return "achats-details";// + idArticle;
}

//doit request aussi l'ID de l'article
@PostMapping("/retire")
public String retire(Model model) {
	//TODO checker que le vendeur ET l'acheteur l'ont marqué comme retiré
	return "redirect:/achats/details";// + idArticle;
}
	
	
}
