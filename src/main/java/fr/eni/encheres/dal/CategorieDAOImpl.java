package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

public class CategorieDAOImpl implements CategorieDAO {

    private JdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void Categorie(Categorie categorie) {

    }

    @Override
    // suppression d'une catégorie
    public void supprimerCategorie(long idCategorie) {

    }

    @Override
    // modification d'une catégorie
    public int modifierCategorie(long idCategorie) {

       String UPDATE = "UPDATE Categorie SET libelle = :libelle WHERE id = :id";

        return this.namedParameterJdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(Categorie.class));

    }

    @Override
    // consulter une catégorie par son id
    public Categorie consulterParId(long Categorie) {

        String FIND_BY_ID = "SELECT id FROM Categorie WHERE id = :id";

        return this.namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, new BeanPropertyRowMapper<>(Categorie.class));
    }

    @Override
    // ajout d'une nouvelle catégorie
    public Enchere ajouterArticle(long idArticle) {

        String ADD_ARTICLE = "INSERT INTO id, libelle, articles FROM Categorie WHERE id = :id";

        return this.namedParameterJdbcTemplate.queryForObject(ADD_ARTICLE, new BeanPropertyRowMapper<>(Enchere.class));
    }
}
