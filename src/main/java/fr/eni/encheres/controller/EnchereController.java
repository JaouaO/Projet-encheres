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

	/**
	 * envoie vers la méthode permettant d'afficher correctement l'article dont l'id
	 * est renseigné en fonction de l'utilisateur connecté et de l'état de l'article
	 * 
	 * @param idArticle
	 * @param model
	 * @param session
	 * @return
	 */
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
					String etatArticleEnVente = article.getEtatVente();
					switch (etatArticleEnVente) {
					case "nonDebutee":
						return "redirect:/portail-encheres";

					case "enCours", "terminee", "livree":
						return "/ventes/details?id=" + idArticle;

					default:
						return "redirect:/portail-encheres";
					}
				} else {
					String etatArticleEnVente = article.getEtatVente();
					switch (etatArticleEnVente) {
					case "enCours":
						return "/achats/details?id=" + idArticle;
					case "terminee", "livree":
						return "/achats/acquisition" + idArticle;
					default:
						return "redirect:/portail-encheres";

					}
				}
			}

		}

		return "redirect:/portail-encheres";
	}

	/**
	 * affiche la page html achats détails pour l'article dont l'id est renseigné
	 * 
	 * @param idArticle
	 * @param model
	 * @param session
	 * @return
	 */
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

	/**
	 * affiche la page html ventes détails pour l'article dont l'id est renseigné
	 * 
	 * @param idArticle
	 * @param model
	 * @param session
	 * @return
	 */
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

	/**
	 * affiche la page html creer nouvelle vente qui permet de creer de nouveaux
	 * articles
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/vente")
	public String afficherVente(Model model, HttpSession session) {
		Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");
		model.addAttribute("categories", this.enchereService.consulterToutCategorie());
		Article article = new Article();
		model.addAttribute("article", article);
		model.addAttribute("utilisateur", utilisateurSession);

		return "creer-nouvelle-vente";
	}

	/**
	 * Obsolète ?
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping({ "/rechercher" })
	public String rechercherEtat(Model model) {

		List<Article> articles = enchereService.consulterParEtat("enchere_ouverte");

		model.addAttribute("articles", articles);

		List<Categorie> categories = this.enchereService.consulterToutCategorie();

		model.addAttribute("categories", categories);

		return "portail-encheres";
	}

	/**
	 * affiche la page html achats acquisiton pour l'article dont l'id est renseigné
	 * 
	 * @param idArticle
	 * @param session
	 * @param model
	 * @return
	 */
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

	/**
	 * initialise l'article créé en html puis l'ajoute en BDD renvoie ensuite au
	 * portail enchères
	 * 
	 * @param article
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/creer-nouvelle-vente")

	public String creerNouvelleVente(@ModelAttribute Article article, Model model, HttpSession session) {

		Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");

		article.setUtilisateur(utilisateur);

		article.setEtatVente("NON_DEBUTEE");

		article.setLieuRetrait(utilisateur.getRetrait(article));

		enchereService.creerArticle(article);

		model.addAttribute("article", article);
		model.addAttribute("utilisateur", utilisateur);

		return "redirect:/portail-encheres";
	}

	/**
	 * affiche la page html enchere non commencee pour l'article dont l'id est
	 * renseigné
	 * 
	 * @param idArticle
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/enchere-non-commence")
	public String afficherVenteNonCommencee(@RequestParam("id") Long idArticle, HttpSession session, Model model) {
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

	/**
	 * met à jour l'article en BDD à partir de ce qui a été renseigné dans la page
	 * html pour l'article renseigné
	 * 
	 * @param article
	 * @param fichierImage
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/enchere-non-commence/modifier")
	public String modifierVenteNonCommencee(@ModelAttribute Article article,
			@RequestParam("fichierImage") MultipartFile fichierImage, HttpSession session, Model model) {
		Utilisateur vendeur = (Utilisateur) session.getAttribute("utilisateurSession");
		if (vendeur == null) {
			model.addAttribute("erreur", "Vous devez être connecté pour modifier une vente.");
			return "connexion";

		}

		if (!enchereService.existeArticle(article.getId())) {
			model.addAttribute("erreur", "L'article demandé n'existe pas.");
			return "portail-encheres";
		}

		Article original = enchereService.consulterArticleParId(article.getId());

		if (!"non_debutee".equalsIgnoreCase(original.getEtatVente())) {
			model.addAttribute("erreur", "La vente ne peut plus être modifiée.");
			return "portail-encheres";
		}

		if (!enchereService.verifierProprietaireArticle(article.getId(), vendeur.getId())) {
			model.addAttribute("erreur", "Vous n'êtes pas autorisé à modifier cette vente.");
			return "portail-encheres";
		}

		article.setEtatVente("non_debutee");
		article.setUtilisateur(vendeur);

		if (!fichierImage.isEmpty()) {
			article.setCheminImg(fichierImage.getOriginalFilename());
		}
		enchereService.mettreAJourArticle(article);

		model.addAttribute("article", article);
		model.addAttribute("categories", enchereService.consulterToutCategorie());
		model.addAttribute("message", "Vente modifiée avec succès.");
		return "portail-encheres";
	}

	/**
	 * Supprime l'article renseigné de la BDD si c'est bien un article que
	 * l'utilisateur connecté peut supprimer
	 * 
	 * @param idArticle
	 * @param session
	 * @param model
	 * @return
	 */
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

	/**
	 * Initialise l'enchère à partir de ce qui a été renseigné sur la page HTML puis
	 * l'ajoute à la BDD si tout est ok
	 * 
	 * @param nouvelleEnchere
	 * @param bindingResult
	 * @param model
	 * @param session
	 * @return
	 */
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

}
