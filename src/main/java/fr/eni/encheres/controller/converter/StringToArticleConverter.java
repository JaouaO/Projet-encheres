package fr.eni.encheres.controller.converter;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bo.Article;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToArticleConverter implements Converter<String, Article> {

	private EnchereService enchereService;

	public StringToArticleConverter(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@Override
	public Article convert(String idArticle) {
		return enchereService.consulterArticleParId(Long.parseLong(idArticle));
	}

}
