package fr.eni.encheres.controller.converter;
 
//import org.springframework.beans.factory.annotation.*;
import org.springframework.core.convert.*;
import org.springframework.core.convert.converter.*;
import org.springframework.stereotype.Component;

import fr.eni.encheres.bll.EnchereService;
import fr.eni.encheres.bll.UtilisateurService;

import java.util.*;
import java.util.stream.*;
 
/** @author Souheil SULTAN */
@Component
public class GenericCollectionConverter3 implements ConditionalGenericConverter {
 

    private EnchereService enchereService;
    private UtilisateurService utilisateurService;
    
    
 
    public GenericCollectionConverter3(EnchereService enchereService,
			UtilisateurService utilisateurService) {
		this.enchereService = enchereService;
		this.utilisateurService = utilisateurService;
	}

	@Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }
 
    @Override
    public boolean matches( TypeDescriptor idType, TypeDescriptor beanType ) {
        final var idClass = idType.isArray() ? idType.getElementTypeDescriptor().getType() : idType.getType();
        return idClass.equals(String.class) && beanType.isCollection();
    }
 
    @Override
    public Object convert( Object idValues, TypeDescriptor idType, TypeDescriptor beanType ) {
        final var beanClass = beanType.getElementTypeDescriptor().getType();
        final var values = idType.isArray() ? Arrays.stream((Object[])idValues) : Stream.of(idValues);
        return values.map( id -> switch (beanClass.getSimpleName()) {
            case "Utilisateur" -> utilisateurService.consulterParId(Long.parseLong((String)id));
            case "Categorie" -> enchereService.consulterCategorieParId(Long.parseLong((String)id));
            case "Article" -> enchereService.consulterArticleParId(Long.parseLong((String)id));
            default -> throw new UnknownFormatConversionException( "Unsupported type: " + beanClass );
        }).collect(Collectors.toList());
    }
 
}
 