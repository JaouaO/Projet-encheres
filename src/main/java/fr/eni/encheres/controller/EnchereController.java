package fr.eni.encheres.controller;
import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.exceptions.BusinessException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;

import java.time.LocalDateTime;
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

	@GetMapping("/achats/acquisition")
	public String afficherAcquisition(@RequestParam("id") Long idArticle, HttpSession session, Model model) {
		Article article = enchereService.consulterArticleParId(idArticle);

		if (article == null) {
			return "redirect:/portail-encheres";
		}

		if (article.getCategorie() != null && article.getCategorie().getLibelle() == null) {
			Categorie categorie = enchereService.consulterCategorieParId(article.getCategorie().getId());
			article.setCategorie(categorie);
		}

		System.out.println("Retrait : " + article.getLieuRetrait());

		model.addAttribute("article", article);
		model.addAttribute("derniereEnchere", enchereService.recupererDerniereEnchere(idArticle));
		model.addAttribute("utilisateurSession", session.getAttribute("utilisateurSession"));

		return "achats-acquisition";
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
		int CreditUserEnSession = utilisateur.getCredit();
		Long ArticleId = enchereForm.getArticleId();
		int montant = enchereForm.getMontantEnchere();
		Article article = enchereService.consulterArticleParId(ArticleId);
		Enchere enchereFormEnchere = new Enchere();
		enchereFormEnchere.setArticle(article);
		enchereFormEnchere.setMontantEnchere(montant);

		if (article == null) {
			model.addAttribute("erreur", "Article non trouvé");
			System.out.println("Article non trouvé");
		}

		//Récupérer dernière enchère
		Enchere derniereEnchere = enchereService.recupererDerniereEnchere(ArticleId);

		if (montant <= 0) {
			model.addAttribute("erreur", "Le montant doit être supérieur à zéro.");
			System.out.println("montant doit être supérieur à zéro");

		}

		if (montant <= article.getMiseAPrix()) {
			model.addAttribute("erreur", "Votre enchère doit être supérieure à la mise à prix.");
			System.out.println("Votre enchère doit être supérieure à la mise à prix.");
		}

		if (derniereEnchere != null && montant <= derniereEnchere.getMontantEnchere()) {
			model.addAttribute("erreur", "Votre enchère doit être supérieure à la meilleure offre actuelle.");
			System.out.println("Votre enchère doit être supérieure à la meilleure offre actuelle.");

		}

		if (montant > CreditUserEnSession) {
			model.addAttribute("erreur", "Vous n'avez pas assez de crédits pour effectuer cette enchère.");
			System.out.println("Vous n'avez pas assez de crédits pour effectuer cette enchère.");

		}

		Enchere nouvelleEnchere = new Enchere();
		nouvelleEnchere.setArticle(article);
		nouvelleEnchere.setMontantEnchere(montant);
		nouvelleEnchere.setUtilisateur(utilisateur);
		nouvelleEnchere.setDateEnchere(LocalDateTime.now());

		enchereService.ajouterEnchere(nouvelleEnchere);

		int nbRetire = nouvelleEnchere.getMontantEnchere();
		long idUtilisateur = utilisateur.getId();
		utilisateurService.retirerCredits(nbRetire, idUtilisateur);

		return "redirect:/achats/details?id=" + article.getId();
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


