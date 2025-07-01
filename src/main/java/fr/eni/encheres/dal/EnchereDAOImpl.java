package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Enchere;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import java.util.List;

public class EnchereDAOImpl implements EnchereDAO {

    private JdbcTemplate namedParameterJdbcTemplate;

    @Override

    // ajout d'une enchère
    public List<Enchere> ajouterEnchere(Enchere enchere) {

        String CREATE = "INSERT INTO Enchere (id, date_enchere, montant_enchere, id_utilisateur, id_article) VALUES (:id, :dateEnchere, :montantEnchere, :idUtilisateur, :idArticle)";

        return this.namedParameterJdbcTemplate.query(CREATE, new BeanPropertyRowMapper<>(Enchere.class));
    }
    @Override

    // consultation de l'utilisateur par son id
    public List<Enchere> consulterParUtilisateur(long idUtilisateur) {

        String FIND_BY_ID = "SELECT id_utilisateur FROM Enchere WHERE id_utilisateur = :idUtilisateur";

        return this.namedParameterJdbcTemplate.query(FIND_BY_ID, new BeanPropertyRowMapper<>(Enchere.class));

    }
    @Override
    // consultation par article
    public List<Enchere> consulterParArticle(long idArticle) {

        String FIND_BY_ARTICLE = "SELECT id_article FROM Enchere WHERE id_article = :idArticle";

        return this.namedParameterJdbcTemplate.query(FIND_BY_ARTICLE, new BeanPropertyRowMapper<>(Enchere.class));

    }
    @Override
    // consulter la liste des enchères
    public List<Enchere> consulterTout() {

        String FIND_ALL = "SELECT dateEnchere, montantEnchere, utilisateur, article FROM Enchere";

        return this.namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
    }
    @Override
    // annulation d'une enchère
    public int annulerEnchere(Enchere enchere) {

        String CANCEL_BY_ID = "DELETE FROM Enchere WHERE id_enchere = 1";

        return this.namedParameterJdbcTemplate.update(CANCEL_BY_ID, new BeanPropertySqlParameterSource(enchere));

    }

}
