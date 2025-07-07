package fr.eni.encheres.bll;


import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDAO;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

	
	
	private final UtilisateurDAO utilisateurDAO;

    public UtilisateurServiceImpl(UtilisateurDAO utilisateurdao) {
        this.utilisateurDAO = utilisateurdao;
    }
	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur ne peut pas être null");
        }
		// Vérification pseudo alphanumérique
		if (!utilisateur.getPseudo().matches("^[a-zA-Z0-9]+$")) {
			throw new IllegalArgumentException("Le pseudo doit être alphanumérique.");
		}

		// pseudo unique
		if (utilisateurDAO.consulterParPseudo(utilisateur.getPseudo()) != null) {
			throw new IllegalArgumentException("Ce pseudo est déjà utilisé.");
		}

		// email unique
		if (utilisateurDAO.consulterParEmail(utilisateur.getEmail()) != null) {
			throw new IllegalArgumentException("Cet email est déjà utilisé.");
		}

		utilisateurDAO.creerUtilisateur(utilisateur);
	}
	@Override
	public void modifierUtilisateur(long idUtilisateur, Utilisateur utilisateur) {
//		if (idUtilisateur <= 0) {
//            throw new IllegalArgumentException("ID utilisateur invalide");
//        }else if(idUtilisateur != utilisateur.getId()) {
//        	throw new IllegalArgumentException("ID utilisateur différent de celui de l'utilisateur modifié");
//        }
         utilisateurDAO.modifierUtilisateur(idUtilisateur, utilisateur);
		
	}


	    
	    @Override
	    public void supprimerUtilisateur(long idUtilisateur) {
	        if (idUtilisateur <= 0) {
	            throw new IllegalArgumentException("ID utilisateur invalide");
	        }
	        utilisateurDAO.supprimerUtilisateur(idUtilisateur);
	    }


	    @Override
	    public void desactiverUtilisateur(long idUtilisateur) {
			 if (idUtilisateur <= 0) {
	            throw new IllegalArgumentException("ID utilisateur invalide");
	        }
	        utilisateurDAO.desactiverUtilisateur(idUtilisateur);
	    }
	    

	    @Override
	    public Utilisateur consulterParId(long idUtilisateur) {
	        if (idUtilisateur <= 0) {
	            throw new IllegalArgumentException("ID utilisateur invalide");
	        }
	        return utilisateurDAO.consulterParId(idUtilisateur);
	    }


	    @Override
	    public void ajouterCredits(int nbAjout, long idUtilisateur) {
	        if (nbAjout <= 0) {
	            throw new IllegalArgumentException ("Nombre de crédits à ajouter doit être positif");
	        }
	        utilisateurDAO.ajouterCredits(nbAjout, idUtilisateur);
	    }

	    @Override
	    public void retirerCredits(int nbRetire, long idUtilisateur) {
	        if (nbRetire <= 0) {
	            throw new IllegalArgumentException("Nombre de crédits à retirer doit être positif");
	        }
			Utilisateur utilisateur = utilisateurDAO.consulterParId(idUtilisateur);
			if (utilisateur.getCredit() < nbRetire) {
        		throw new IllegalStateException("Crédits insuffisants : solde actuel = " 
                                        + utilisateur.getCredit() + ", demandé = " + nbRetire);
    }
	        utilisateurDAO.retirerCredits(nbRetire, idUtilisateur);
	    }


		@Override
		public void annulerVenteParUtilisateur(long idArticle, long idUtilisateur) {
	    		      
	    	
	    	/*List<Enchere> encheres = enchereDAO.consulterParArticle(idArticle);
	        for (Enchere enchere : encheres) {
	            Utilisateur encherisseur = enchere.getUtilisateur();
	            utilisateurDAO.ajouterCredits(enchere.getMontantEnchere(), encherisseur.getId());
//	        }*/
//	        this.articleDAO.annulerVente(idArticle);
	    }

		@Override
		public Utilisateur verifierConnexion(String pseudo, String motDePasse) {
	    	Utilisateur utilisateur = utilisateurDAO.consulterParPseudo(pseudo);
	    		if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
	       		 return utilisateur;
	    }
	    	return null;

	}

	}
