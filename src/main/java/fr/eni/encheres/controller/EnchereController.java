package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
@GetMapping("/detail")
public String getMethodName(@RequestParam(name = "id") long idArticle, Model model) {
	//check l'utilisateur pour voir si c'est achat ou vente
    return "detail-vente";
}

@GetMapping("/vente")
public String getMethodName( Model model) {
    return "creer-nouvelle-vente";
}


	
	
}
