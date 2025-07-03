package fr.eni.encheres.controller.converter;

import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToUtilisateurConverter implements Converter<String, Utilisateur> {

	private UtilisateurService utilisateurService;



	public StringToUtilisateurConverter(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}



	@Override
	public Utilisateur convert(String idUtilisateur) {
		return utilisateurService.consulterParId(Long.parseLong(idUtilisateur));
	}

}
