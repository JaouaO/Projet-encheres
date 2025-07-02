package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void Categorie(Categorie categorie) {

    }

    @Override
    // suppression d'une catégorie
    public void supprimerCategorie(long idCategorie) {

    }

    @Override
    // modification d'une catégorie
    public void modifierCategorie(long idCategorie) {

        String UPDATE = "UPDATE Categorie SET libelle = :libelle WHERE id = :id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idCategorie);

        this.namedParameterJdbcTemplate.update(UPDATE, parameterSource);

    }

    @Override
    // consulter une catégorie par son id
    public Categorie consulterParId(long idCategorie) {

        String FIND_BY_ID = "SELECT id, libelle FROM Categorie WHERE id = :id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idCategorie);

        return this.namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, parameterSource, new BeanPropertyRowMapper<>(Categorie.class));
    }

    @Override
    // ajout d'une nouvelle catégorie
    public Categorie ajouterArticle(Article article) {

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", article.getId());

        String ADD_ARTICLE = "INSERT INTO Categorie (id) VALUES (:id)";

        return this.namedParameterJdbcTemplate.queryForObject(ADD_ARTICLE, parameterSource, new BeanPropertyRowMapper<>(Categorie.class));
    }
}
