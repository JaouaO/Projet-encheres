package fr.eni.encheres.controller;
import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.exceptions.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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
    public String afficherDetailsAchats(@RequestParam(name = "id") long idArticle, Model model, HttpSession session) {
    	
    	
    	Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

    	if(utilisateurSession==null) {
    		return  "redirect:/accueil";
    	}
    	
        if (idArticle > 0) {
            Article article = enchereService.consulterArticleParId(idArticle);
            if (article != null) {
                model.addAttribute("article", article);
                
                Categorie categorieArticle = enchereService.consulterCategorieParId(article.getCategorie().getId());
                model.addAttribute("categorieArticle", categorieArticle);

                Enchere derniereEnchere = enchereService.recupererDerniereEnchere(idArticle);

                model.addAttribute("derniereEnchere", derniereEnchere);
                Enchere enchere = new Enchere();
                enchere.setArticle(article);
                
                model.addAttribute("enchere",enchere);
                return "achats-details";
            }
        }
        return "redirect:/portail-encheres";
    }

    //pas sur de comment on affiche le nom de l'article avec get, ça suffit comme ça?
@GetMapping("/ventes/details")
public String afficherDetailsVentes(@RequestParam(name = "id") long idArticle, Model model, HttpSession session) {
	
	
	Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

	if(utilisateurSession==null) {
		return  "redirect:/accueil";
	}
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

		if (article.getUtilisateur() != null) {
			Utilisateur vendeur = utilisateurService.consulterParId(article.getUtilisateur().getId());
			article.setUtilisateur(vendeur);
		}


		model.addAttribute("article", article);
		model.addAttribute("derniereEnchere", enchereService.recupererDerniereEnchere(idArticle));
		model.addAttribute("utilisateurSession", session.getAttribute("utilisateurSession"));

		return "achats-acquisition";
	}






	@PostMapping("/creer-nouvelle-vente")
	public String getMethodName(@ModelAttribute Article article, Model model) {
	//modifier pour prendre l'user en session
		Utilisateur utilisateur = utilisateurService.consulterParId(1);
		article.setUtilisateur(utilisateur);

		article.setEtatVente("NON_DEBUTEE");

		article.setLieuRetrait(utilisateur.getRetrait());

		enchereService.creerArticle(article); // voir pour le chemin de l'image...

		return "redirect:/portail-encheres";
	}

// pour récupérer l'enchère avec les attributs du formulaire
//	public static class EnchereFormulaire {
//		private Long articleId;
//		private int montantEnchere;
//
//
//		public Long getArticleId() { return articleId; }
//		public void setArticleId(Long articleId) { this.articleId = articleId; }
//
//
//		public int getMontantEnchere() { return montantEnchere; }
//		public void setMontantEnchere(int montantEnchere) { this.montantEnchere = montantEnchere; }
//	}



	@GetMapping("/vente/modifier")
	public String afficherFormulaireModification(@RequestParam("id") Long idArticle, HttpSession session, Model model) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		if (utilisateur == null) {
			return "redirect:/connexion";
		}

		Article article = enchereService.consulterArticleParId(idArticle);
		if (article == null || article.getUtilisateur() == null || !Objects.equals(article.getUtilisateur().getId(), utilisateur.getId())) {
			return "redirect:/portail-encheres";
		}

		if (!"non_debutee".equalsIgnoreCase(article.getEtatVente())) {
			model.addAttribute("erreur", "Vous ne pouvez modifier que les ventes non débutées.");
			return "redirect:/ventes/details?id=" + idArticle;
		}

		model.addAttribute("article", article);
		model.addAttribute("categories", enchereService.consulterToutCategorie());
		return "modifier-vente";
	}


	@PostMapping("/vente/modifier")
	public String modifierVente(@ModelAttribute Article article, HttpSession session) {
		Utilisateur vendeur = (Utilisateur) session.getAttribute("utilisateurSession");
		Article original = enchereService.consulterArticleParId(article.getId());

		//!article existe+!articleappartient à utilisateur connecté(vendeur)
		if (original == null || original.getUtilisateur() == null || !Objects.equals(original.getUtilisateur().getId(), vendeur.getId())) {
			return "redirect:/portail-encheres";
		}

		if (!"NON_DEBUTEE".equalsIgnoreCase(original.getEtatVente())) {
			return "redirect:/ventes/details?id=" + article.getId();
		}

		article.setEtatVente("NON_DEBUTEE");
		article.setUtilisateur(vendeur);
		enchereService.mettreAJourArticle(article);

		return "redirect:/ventes/details?id=" + article.getId();
	}

	@PostMapping("/encherir")
	public String encherir(@Valid @ModelAttribute Enchere nouvelleEnchere, BindingResult bindingResult, Model model, HttpSession session) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		nouvelleEnchere.setUtilisateur(utilisateur);
		nouvelleEnchere.setDateEnchere(LocalDateTime.now());
		nouvelleEnchere.setArticle(enchereService.consulterArticleParId(nouvelleEnchere.getArticle().getId()));
		try {
			enchereService.ajouterEnchere(nouvelleEnchere);
			return "redirect:/achats/details?id=" + nouvelleEnchere.getArticle().getId();
		} catch (BusinessException e) {
			model.addAttribute("errorMessages", e.getMessages());
//			e.getMessages().forEach(m->{
//				ObjectError error = new ObjectError("", m);
//			bindingResult.addError(error);
//	});
	
			model.addAttribute("article", nouvelleEnchere.getArticle());
            
            Categorie categorieArticle = enchereService.consulterCategorieParId(nouvelleEnchere.getArticle().getCategorie().getId());
            model.addAttribute("categorieArticle", categorieArticle);

            Enchere derniereEnchere = enchereService.recupererDerniereEnchere(nouvelleEnchere.getArticle().getId());

            model.addAttribute("derniereEnchere", derniereEnchere);
            Enchere enchere = new Enchere();
            enchere.setArticle(nouvelleEnchere.getArticle());
            
            model.addAttribute("enchere",enchere);
            return "achats-details";
		}

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


