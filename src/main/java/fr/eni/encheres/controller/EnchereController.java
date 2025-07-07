package fr.eni.encheres.controller;
import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;

import java.util.List;


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

    @GetMapping({ "/rechercher" })
    public String rechercherEtat (Model model) {

        List<Article> articles = enchereService.consulterParEtat("enchere_ouverte");

        model.addAttribute("articles", articles);

        List<Categorie> categories = this.enchereService.consulterToutCategorie();

        model.addAttribute("categories", categories);

        return "portail-encheres";
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

// pour récupérer l'enchère avec les attributs du formulaire
	public static class EnchereFormulaire {
		private Long articleId;
		private int montantEnchere;


		public Long getArticleId() { return articleId; }
		public void setArticleId(Long articleId) { this.articleId = articleId; }

		public int getMontantEnchere() { return montantEnchere; }
		public void setMontantEnchere(int montantEnchere) { this.montantEnchere = montantEnchere; }
	}

	@PostMapping("/encherir")
	public String encherir(@ModelAttribute EnchereFormulaire enchereForm, Model model, HttpSession session) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		Long articleId = enchereForm.getArticleId();
		int montant = enchereForm.getMontantEnchere();

		Article article = enchereService.consulterArticleParId(articleId);
		if (article == null) {
			model.addAttribute("error", "Article non trouvé");
			return "achats-details"; // nom de ta vue
		}

		//Récupérer dernière enchère
		Enchere derniereEnchere = enchereService.recupererDerniereEnchere(articleId);

		if (montant <= 0) {
			model.addAttribute("error", "Le montant doit être supérieur à zéro.");
			return "achats-details";
		}

		if (montant <= article.getMiseAPrix()) {
			model.addAttribute("error", "Votre enchère doit être supérieure à la mise à prix.");
			return "achats-details";
		}

		if (derniereEnchere != null && montant <= derniereEnchere.getMontantEnchere()) {
			model.addAttribute("error", "Votre enchère doit être supérieure à la meilleure offre actuelle.");
			return "achats-details";
		}

		if (montant < utilisateur.getCredit()) {
			model.addAttribute("error", "Vous n'avez pas assez de crédits pour effectuer cette enchère.");
			return "achats-details";
		}

		Enchere nouvelleEnchere = new Enchere();
		nouvelleEnchere.setArticle(article);
		nouvelleEnchere.setMontantEnchere(montant);
		nouvelleEnchere.setUtilisateur(utilisateur);
		nouvelleEnchere.setDateEnchere(java.time.LocalDateTime.now());

		enchereService.ajouterEnchere(nouvelleEnchere);

		return "redirect:/achats/details" + articleId;
	}

//doit request aussi l'ID de l'article
//@PostMapping("/retire")
//public String retire(Model model) {
    // TODO checker que le vendeur ET l'acheteur l'ont marqué comme retiré
 //   return "redirect:/achats/details";// + idArticle;
//}

//

}
//doit request aussi l'ID de l'article
//	@PostMapping("/retire")
//	public String retire(Model model) {
//		// TODO checker que le vendeur ET l'acheteur l'ont marqué comme retiré
//		return "redirect:/achats/details";// + idArticle;
//	}


