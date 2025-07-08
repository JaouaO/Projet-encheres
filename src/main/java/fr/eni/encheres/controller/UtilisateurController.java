package fr.eni.encheres.controller;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Article;

import fr.eni.encheres.bo.Categorie;


import java.time.Clock;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("utilisateurEnSession")
@Controller
public class UtilisateurController {

    private EnchereService enchereService;

    private UtilisateurService utilisateurService;

    public UtilisateurController(EnchereService enchereService, UtilisateurService utilisateurService) {
        this.enchereService = enchereService;
        this.utilisateurService = utilisateurService;

    }

    @GetMapping({"/", "/accueil"})
    public String afficherAccueil(
            @RequestParam(name = "idCategorie", required = false, defaultValue = "0") Long idCategorie,
            @RequestParam(name = "text", required = false, defaultValue = "") String text, Model model) {

        List<Article> articles;

        if (idCategorie != 0 && !text.isBlank()) {
            articles = enchereService.consulterParCategorieEtRecherche(idCategorie, text);
        } else if (!text.isBlank()) {
            articles = enchereService.consulterParRecherche(text);
            System.out.println("je consulte uniquement par recherche");
        } else if (idCategorie != 0) {
            articles = enchereService.consulterParCategorie(idCategorie);
        } else {
            articles = enchereService.consulterToutArticle();
            System.out.println("je consulte tout");
        }

        model.addAttribute("articles", articles);

        model.addAttribute("categories", enchereService.consulterToutCategorie());
        model.addAttribute("idCategorie", idCategorie);
        model.addAttribute("text", text);

        return "index";
    }

    @GetMapping("/connexion")
    public String afficherConnexion(Model model) {
        return "connexion";
    }


    @PostMapping("/connexion")
    public String connecterUtilisateur(@RequestParam("pseudo") String pseudo,

                                       @RequestParam("motDePasse") String motDePasse, Model model, HttpSession session) {

        Utilisateur utilisateur = utilisateurService.verifierConnexion(pseudo, motDePasse);

        if (utilisateur != null) {
            session.setAttribute("utilisateurSession", utilisateur);
            return "redirect:/portail-encheres";
        } else {
            model.addAttribute("erreur", "Pseudo ou mot de passe incorrect.");
            return "connexion";
        }
    }

    @GetMapping("/portail-encheres")
    public String afficherPortail(HttpSession session,
                                  @RequestParam(name="checkbox", required = false, defaultValue = "[encheresOuvertes]") String[] checkbox,
                                  @RequestParam(name = "idCategorie", required = false, defaultValue = "0") Long idCategorie,
                                  @RequestParam(name = "text", required = false, defaultValue = "") String text,
                                  Model model) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
        long idUtilisateurSession = utilisateur.getId();

        if (utilisateur == null) {
            return "redirect:/connexion";
        }
        System.out.println(checkbox);
        //Liste finale qui sera affichée en fonction de la comparaison des deux listes ci-dessous
        List<Article> articlesAAfficher = new ArrayList<>();

        //une première liste qui contient les articles filtrés selon les cases à cocher
        List<Article> articlesCheckbox = new ArrayList<>();

        for (int i = 0; i < checkbox.length; i++) {

            if (checkbox[i].isEmpty()) {
                articlesCheckbox.addAll(enchereService.consulterToutArticle());
            }

            if (checkbox[i].equals("[encheresOuvertes]")) {
                articlesCheckbox.addAll(enchereService.consulterParIdUtilisateurEtEnchereOuverte(utilisateur.getId()));

            }
            if (checkbox[i].equals("[encheresEnCours]")) {
                articlesCheckbox.addAll(enchereService.consulterParIdUtilisateurEtEtatVente(utilisateur.getId(), "enCours"));

            }
            if (checkbox[i].equals("[encheresRemportées]")) {
                List<Article> ArticlesEtatTermine = enchereService.consulterParEtat("terminee");
                for (Article article : ArticlesEtatTermine) {
                    long idArticle = article.getId();
                    List<Enchere> encheresParArticle = enchereService.consulterParIdArticle(idArticle);
                    for (Enchere enchere : encheresParArticle) {
                        Enchere derniereEnchere = enchereService.recupererDerniereEnchere(idArticle);
                        long IdDernierEncherisseur = derniereEnchere.getUtilisateur().getId();
                        if (IdDernierEncherisseur == idUtilisateurSession) {
                            articlesCheckbox.add(article);
                        }
                    }
                }
            }
            if (checkbox[i].equals("[ventesEnCours]")) {
                List<Article> ArticleVentesEnCours = enchereService.consulterParEtat("enCours");
                for (Article article : ArticleVentesEnCours) {
                    long idUtilisateurVendeur = article.getUtilisateur().getId();
                    if (idUtilisateurVendeur == idUtilisateurSession) {
                        articlesCheckbox.add(article);
                    }
                }
            }
            if (checkbox[i].equals("[ventesNonDebutees]")) {
                List<Article> ArticleVenteNonDebutees = enchereService.consulterParEtat("nonDebutee");
                for (Article article : ArticleVenteNonDebutees) {
                    long idUtilisateurVendeur = article.getUtilisateur().getId();
                    if (idUtilisateurVendeur == idUtilisateurSession) {
                        articlesCheckbox.add(article);
                    }
                }
            }
            if (checkbox[i].equals("[ventesTerminees]")) {
                List<Article> ArticleVentesTerminees = enchereService.consulterParEtat("terminee");
                for (Article article : ArticleVentesTerminees) {
                    long idUtilisateurVendeur = article.getUtilisateur().getId();
                    if (idUtilisateurVendeur == idUtilisateurSession) {
                        articlesCheckbox.add(article);
                    }
                }
                List<Article> ArticleVentesLivrees = enchereService.consulterParEtat("livree");
                for (Article article : ArticleVentesLivrees) {
                    long idUtilisateurVendeur = article.getUtilisateur().getId();
                    if (idUtilisateurVendeur == idUtilisateurSession) {
                        articlesCheckbox.add(article);
                    }
                }
            }

            //une seconde liste qui contient les articles filtrés selon leur catégories et/ou la recherche par mot clé

            List<Article> articlesAutresFiltres = new ArrayList<>();

            if (idCategorie != 0 && !text.isBlank()) {
                articlesAutresFiltres.addAll(enchereService.consulterParCategorieEtRecherche(idCategorie, text));
            } else if (!text.isBlank()) {
                articlesAutresFiltres.addAll(enchereService.consulterParCategorieEtRecherche(idCategorie, text));
            } else if (idCategorie != 0) {
                articlesAutresFiltres.addAll(enchereService.consulterParCategorie(idCategorie));
            } else {
                articlesAutresFiltres.addAll(enchereService.consulterToutArticle());
            }

            // comparaison des deux listes pour compléter la liste à afficher avec uniquement les articles communs aux deux listes

        for (Article article : articlesCheckbox) {
            for(Article article2 : articlesAutresFiltres) {
                if (article.getId() == article2.getId()) {
                    articlesAAfficher.add(article);
                }
            }
        }
        }

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("articlesAAfficher", articlesAAfficher);
        model.addAttribute("categories", enchereService.consulterToutCategorie());
        model.addAttribute("idCategorie", idCategorie);
        model.addAttribute("text", text);

        return "portail-encheres";
    }


    @GetMapping("/profil")
    public String afficherProfil(@RequestParam(name = "id", required = false, defaultValue = "-1") Long IdUtilisateur,
                                 HttpSession session, Model model) {
        Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");

        if (IdUtilisateur == -1 || IdUtilisateur == utilisateurSession.getId()) {
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("afficherModifier", true);
        } else {
            Utilisateur utilisateur = utilisateurService.consulterParId(IdUtilisateur);
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("afficherModifier", false);
        }

        return "profil";
    }


    @GetMapping("/inscription")
    public String afficherCreerCompte(Model model) {
        Utilisateur utilisateur = new Utilisateur();
        model.addAttribute("utilisateur", utilisateur);

        return "creer-compte";
    }

    @PostMapping("/inscription")
    public String creerCompte(@ModelAttribute("utilisateur") Utilisateur utilisateur, Model model) {
        try {
            utilisateurService.creerUtilisateur(utilisateur);
            return "redirect:/portail-encheres";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
            return "creer-compte";
        }
    }


    @GetMapping("/profil/modifier")
    public String afficherModifierProfil(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
        model.addAttribute("utilisateur", utilisateur);
        return "modifier-profil";
    }

    @PostMapping("/profil/modifier")
    public String modifierProfil(@ModelAttribute("utilisateur") Utilisateur utilisateurModifie, HttpSession session,
                                 Model model) {

        System.out.println(utilisateurModifie);

        Utilisateur utilisateurSession = (Utilisateur) session.getAttribute("utilisateurSession");
        try {

            System.out.println(utilisateurSession);
            utilisateurService.modifierUtilisateur(utilisateurSession.getId(), utilisateurModifie);
            session.setAttribute("utilisateurSession", utilisateurModifie);

        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
        }
        model.addAttribute("afficherModifier", true);
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("message", "Profil mis à jour !!!!!!");
        return "profil";
    }


    @GetMapping("/session-cloture")
    public String finSession(SessionStatus sessionStatus, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateurSession");

        if (utilisateur != null) {
            session.setAttribute("utilisateurSession", null);
        }
        sessionStatus.setComplete();

        return "redirect:/accueil";
    }

    @ModelAttribute("membreEnSession")
    public Utilisateur addUtilisateurEnSession() {
        System.out.println("Add membre en session");
        return new Utilisateur();
    }

//	@PostMapping("/portail-encheres")
//	public String filtrerArticles(@RequestParam("type") String type,
//			@RequestParam(value = "categorie", required = false) String categorie, Model model) {
//
//		List<Article> articles;
//
//		// Récupération des articles selon le type (achat ou vente)
//		if ("vente".equals(type)) {
//			articles = enchereService.consulterParEtat("Mes ventes");
//		} else {
//			articles = enchereService.consulterParEtat("Enchère ouverte");
//		}
//
//		// Si une catégorie est sélectionnée, on filtre avec une boucle
//		if (categorie != null && !categorie.isEmpty()) {
//			List<Article> articlesFiltres = new ArrayList<>();
//
//			for (Article article : articles) {
//				if (article.getCategorie().getLibelle().equalsIgnoreCase(categorie)) {
//					articlesFiltres.add(article);
//				}
//			}
//		}
//		// articles = articlesFiltres; // problème à regler
//
//		// On prépare les données pour l'affichage dans la vue
//		model.addAttribute("articles", articles);
//		model.addAttribute("categories", enchereService.consulterToutCategorie());
//		model.addAttribute("type", type);
//
//		return "portail-encheres";
}



