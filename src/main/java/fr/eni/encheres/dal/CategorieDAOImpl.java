package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    

    public CategorieDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
    public void creerCategorie(Categorie categorie) {

        String creerCategorie = "INSERT INTO Categorie (id, libelle) VALUES (:id, :libelle)";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("libelle", categorie.getLibelle());
        parameterSource.addValue("id", categorie.getId());

        namedParameterJdbcTemplate.update(creerCategorie, parameterSource);

    }

    @Override
    // modification d'une catégorie
    public void modifierCategorie(long idCategorie) {

        String modifCategorie = "UPDATE Categorie SET libelle = :libelle WHERE id = :id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idCategorie);

        this.namedParameterJdbcTemplate.update(modifCategorie, parameterSource);

    }

    @Override
    // consulter une catégorie par son id
    public Categorie consulterParId(long idCategorie) {

        String trouverParId = "SELECT id, libelle FROM Categorie WHERE id = :id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idCategorie);

        return this.namedParameterJdbcTemplate.queryForObject(trouverParId, parameterSource, new BeanPropertyRowMapper<>(Categorie.class));
    }

    @Override
    // ajout d'une nouvelle catégorie
    public Categorie ajouterArticle(Article article) {

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", article.getId());

        String integrerArticle = "INSERT INTO Categorie (id) VALUES (:id)";

        return this.namedParameterJdbcTemplate.queryForObject(integrerArticle, parameterSource, new BeanPropertyRowMapper<>(Categorie.class));
    }

	@Override
	public List<Categorie> consulterTout() {
		String trouverToutesLesCategories = """
                SELECT id, libelle
                FROM categorie
                """;
		return namedParameterJdbcTemplate.query(trouverToutesLesCategories,new BeanPropertyRowMapper<>(Categorie.class));
	}

    @Override
    // suppression d'une catégorie
    public void supprimerCategorie(long idCategorie) {

        String supprCategorie = "DELETE Categorie  WHERE id = :id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idCategorie);

        this.namedParameterJdbcTemplate.update(supprCategorie, parameterSource);

    }

}
