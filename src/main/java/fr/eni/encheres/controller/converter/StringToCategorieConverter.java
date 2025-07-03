package fr.eni.encheres.controller.converter;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Categorie;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie> {

	private EnchereService enchereService;

	public StringToCategorieConverter(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@Override
	public Categorie convert(String idCategorie) {
		return enchereService.consulterCategorieParId(Long.parseLong(idCategorie));
	}

}
