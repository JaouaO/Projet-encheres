package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Utilisateur;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void creerUtilisateur(Utilisateur utilisateur) {

    }

    @Override
    public void modifierUtilisateur(Utilisateur utilisateur, long idUtilisateur) {

    }

    @Override
    public void supprimerUtilisateur(long idUtilisateur) {

    }

    @Override
    public void desactiverUtilisateur(long idUtilisateur) {

    }

    @Override
    public Utilisateur consulterParId(long idUtilisateur) {
        return null;
    }

    @Override
    public void ajouterCredits(int nbAjout) {

    }

    @Override
    public void retirerCredits(int nbRetire) {

    }

    @Override
    public void ajouterArticle(long idArticle) {

    }
}

