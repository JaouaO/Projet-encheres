package fr.eni.encheres.controller;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


//@SessionAttributes("utilisateurEnSession")
@Controller
public class EnchereController {

    private EnchereService enchereService;

    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    //pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
@GetMapping("/ventes/details")
public String afficherDetailsVentes(@RequestParam(name = "id") long idArticle, Model model) {
	//check l'utilisateur pour voir si c'est achat ou vente
    return "ventes-details";
}

//pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
@GetMapping("/achats/details")
public String afficherDetailsAchats( @RequestParam(name = "id") long idArticle, Model model) {
    if (idArticle > 0) {
        Article article = enchereService.consulterArticleParId(idArticle);
        if (article != null) {
            // Ajout de l'instance dans le modèle
            model.addAttribute("article", article);

            return "achats-details";
        } else
            System.out.println("Cet article n'existe pas");
            return "redirect:index";
    } else {
        System.out.println("Cet article n'existe pas");
        return "redirect:index";
    }


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
