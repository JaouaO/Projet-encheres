package fr.eni.encheres.controller;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;


@SessionAttributes("utilisateurEnSession")


@Controller
public class EnchereController {
  
  
    private EnchereService enchereService;
    private UtilisateurService utilisateurService;



public EnchereController(EnchereService enchereService, UtilisateurService utilisateurService) {
		this.enchereService = enchereService;
		this.utilisateurService = utilisateurService;
	}



    @GetMapping("/achats/details")
    public String afficherDetailsAchats(@RequestParam(name = "id") long idArticle, Model model) {
        if (idArticle > 0) {
            Article article = enchereService.consulterArticleParId(idArticle);
            if (article != null) {
                model.addAttribute("article", article);
                

                Categorie categorieArticle = enchereService.consulterCategorieParId(article.getCategorie().getId());
                model.addAttribute("categorieArticle", categorieArticle);

                Enchere derniereEnchere = enchereService.recupererDerniereEnchere(idArticle);

                model.addAttribute("derniereEnchere", derniereEnchere);

                return "achats-details";
            }
        }
        return "redirect:index";
    }
  
    //pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
@GetMapping("/ventes/details")
public String afficherDetailsVentes(@RequestParam(name = "id") long idArticle, Model model) {
	//check l'utilisateur pour voir si c'est achat ou vente
    return "ventes-details";
}
  
  

@GetMapping("/vente")
public String afficherVente( Model model) {
	model.addAttribute("categories", this.enchereService.consulterToutCategorie());
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
		
		enchereService.creerArticle(article);

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
