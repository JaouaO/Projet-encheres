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
import org.springframework.web.multipart.MultipartFile;

@SessionAttributes("utilisateurEnSession")

@Controller
public class EnchereController {

	private EnchereService enchereService;
	private UtilisateurService utilisateurService;

	public EnchereController(EnchereService enchereService, UtilisateurService utilisateurService) {
		this.enchereService = enchereService;
		this.utilisateurService = utilisateurService;
	}

	@GetMapping("/details")
	public String AfficherDetails(@RequestParam(name = "id") long idArticle, Model model, HttpSession session) {

		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");
		if (utilisateurSession == null) {
			return "redirect:/accueil";
		}
		if (idArticle > 0) {

			Article article = enchereService.consulterArticleParId(idArticle);
			if (article != null) {
				if (article.getUtilisateur().getId() == utilisateurSession.getId()) {
					String etatArticleEnVente = article.getEtatVente().toLowerCase();
					System.out.println(article);
					switch (etatArticleEnVente) {
					case "nondebutee","non_debutee":
						return "redirect:/enchere-non-commence?id=" + idArticle;

					case  "encours","en_cours", "terminee", "livree":
						return "redirect:/ventes/details?id=" + idArticle;

					default:
						return "redirect:/portail-encheres";
					}
				} else {
					String etatArticleEnVente = article.getEtatVente();
					switch (etatArticleEnVente) {
					case  "encours", "en_cours":
						return "redirect:/achats/details?id=" + idArticle;
					case "terminee", "livree":
						return "redirect:/achats/acquisition?id=" + idArticle;
					default:
						return "redirect:/portail-encheres";
						
					}
				}
			}

		}

		return "redirect:/portail-encheres";
	}

	@GetMapping("/achats/details")
	public String afficherDetailsAchats(@RequestParam(name = "id") long idArticle, Model model, HttpSession session) {

		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

		if (utilisateurSession == null) {
			return "redirect:/accueil";
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

				model.addAttribute("enchere", enchere);
				return "achats-details";
			}
		}
		return "redirect:/portail-encheres";
	}

	@GetMapping("/ventes/details")
	public String afficherDetailsVentes(@RequestParam(name = "id") long idArticle, Model model, HttpSession session) {

		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

		if (utilisateurSession == null) {
			return "redirect:/accueil";
		}

		if (idArticle > 0) {
			Article article = enchereService.consulterArticleParId(idArticle);
			if (article != null) {
				model.addAttribute("article", article);
				Enchere derniereEnchere = enchereService.recupererDerniereEnchere(idArticle);
				Categorie categorieArticle = enchereService.consulterCategorieParId(article.getCategorie().getId());
				model.addAttribute("categorieArticle", categorieArticle);
				model.addAttribute("derniereEnchere", derniereEnchere);

				if (article.getEtatVente().equals("terminee") || article.getEtatVente().equals("livree")) {
					Utilisateur acquereur = derniereEnchere.getUtilisateur();
					model.addAttribute("acquereur", acquereur);
					model.addAttribute("finis", true);
				}
				return "ventes-details";
			}
		}

		return "redirect:/accueil";
	}

	@GetMapping("/vente")
	public String afficherVente(Model model) {
		model.addAttribute("categories", this.enchereService.consulterToutCategorie());
		Article article = new Article();
		model.addAttribute("article", article);

		return "creer-nouvelle-vente";
	}

	@GetMapping({ "/rechercher" })
	public String rechercherEtat(Model model) {

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
		// modifier pour prendre l'user en session
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


	@GetMapping("/enchere-non-commence")
	public String afficherVenteNonCommencee(@RequestParam("id") Long idArticle,
											HttpSession session,
											Model model) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
		if (utilisateur == null) {
			model.addAttribute("erreur", "Vous devez être connecté pour accéder à cette page.");
			return "connexion";
		}
		Article article = enchereService.consulterArticleParId(idArticle);
		model.addAttribute("article", article);
		model.addAttribute("categories", enchereService.consulterToutCategorie());
		return "enchere-non-commence";
	}


	@PostMapping("/enchere-non-commence/modifier")
	public String modifierVenteNonCommencee(@ModelAttribute Article article,
											@RequestParam("fichierImage") MultipartFile fichierImage,
											HttpSession session,
											Model model) {
		Utilisateur vendeur = (Utilisateur) session.getAttribute("utilisateurSession");
		if (vendeur == null) {
			model.addAttribute("erreur", "Vous devez être connecté pour modifier une vente.");
			return "connexion";

		}

		if (!enchereService.existeArticle(article.getId())) {
			model.addAttribute("erreur", "L'article demandé n'existe pas.");
			System.out.println("no article");
			return "portail-encheres";
		}

		Article original = enchereService.consulterArticleParId(article.getId());
		// vente pas encore commencée
		if (!"non_debutee".equalsIgnoreCase(original.getEtatVente())) {
			model.addAttribute("erreur", "La vente ne peut plus être modifiée.");
			System.out.println("non modifiable");
			return "portail-encheres";
		}

		// puis utilisateur connecté est bien le propriétaire de l'article
		if (!enchereService.verifierProprietaireArticle(article.getId(), vendeur.getId())) {
			model.addAttribute("erreur", "Vous n'êtes pas autorisé à modifier cette vente.");
			System.out.println("pas proprio");
			return "portail-encheres";
		}

		//fini par mise à jour article
		article.setEtatVente("non_debutee");
		article.setUtilisateur(vendeur);
		// laisse l’image existante si aucune nouvelle n’est envoyée
		if (!fichierImage.isEmpty()) {
			article.setCheminImg(fichierImage.getOriginalFilename());
		}
		enchereService.mettreAJourArticle(article);

		model.addAttribute("article", article);
		model.addAttribute("categories", enchereService.consulterToutCategorie());
		model.addAttribute("message", "Vente modifiée avec succès.");
		System.out.println("modif succes");
		return "portail-encheres";
	}


	@GetMapping("/annuler-vente")
	public String annulerVente(@RequestParam("id") Long idArticle, HttpSession session, Model model) {
		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");

		if (utilisateur == null) {
			model.addAttribute("erreur", "Vous devez être connecté pour accéder à cette action.");
			return "connexion";
		}
		Article article = enchereService.consulterArticleParId(idArticle);


		if (article == null || !Objects.equals(article.getUtilisateur().getId(), utilisateur.getId())) {
			model.addAttribute("erreur", "Vous n'avez pas le droit d'annuler cette vente.");
			return "redirect:/portail-encheres";
		}

		if (!"non_debutee".equalsIgnoreCase(article.getEtatVente())) {
			model.addAttribute("erreur", "Impossible d'annuler une vente déjà commencée.");
			return "redirect:/portail-encheres";
		}

		enchereService.annulerVente(article.getId());

		model.addAttribute("message", "Vente annulée avec succès.");
		return "redirect:/accueil";
	}

	@PostMapping("/encherir")
	public String encherir(@Valid @ModelAttribute Enchere nouvelleEnchere, BindingResult bindingResult, Model model,
			HttpSession session) {
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

			Categorie categorieArticle = enchereService
					.consulterCategorieParId(nouvelleEnchere.getArticle().getCategorie().getId());
			model.addAttribute("categorieArticle", categorieArticle);

			Enchere derniereEnchere = enchereService.recupererDerniereEnchere(nouvelleEnchere.getArticle().getId());

			model.addAttribute("derniereEnchere", derniereEnchere);
			Enchere enchere = new Enchere();
			enchere.setArticle(nouvelleEnchere.getArticle());

			model.addAttribute("enchere", enchere);
			return "achats-details";
		}

	}

//doit request aussi l'ID de l'article
//@PostMapping("/retire")
//public String retire(Model model) {
	// TODO checker que le vendeur ET l'acheteur l'ont marqué comme retiré
	// return "redirect:/achats/details";// + idArticle;
//}

//

}
//doit request aussi l'ID de l'article
//	@PostMapping("/retire")
//	public String retire(Model model) {
//		// TODO checker que le vendeur ET l'acheteur l'ont marqué comme retiré
//		return "redirect:/achats/details";// + idArticle;
//	}
