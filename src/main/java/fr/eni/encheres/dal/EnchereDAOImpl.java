package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	

	public EnchereDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override

	// ajout d'une enchère
	public List<Enchere> ajouterEnchere(Enchere enchere) {

		String creerEnchere = "INSERT INTO Enchere (id, date_enchere, montant_enchere, id_utilisateur, id_article) VALUES (:id, :dateEnchere, :montantEnchere, :idUtilisateur, :idArticle)";

		return this.namedParameterJdbcTemplate.query(creerEnchere, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override

	// consultation de l'utilisateur par son id
	public List<Enchere> consulterParUtilisateur(long idUtilisateur) {

		String trouverParUtilisateur = "SELECT id_utilisateur FROM Enchere WHERE id_utilisateur = :idUtilisateur";

		return this.namedParameterJdbcTemplate.query(trouverParUtilisateur, new BeanPropertyRowMapper<>(Enchere.class));

	}

	@Override
	// consultation par article
	public List<Enchere> consulterParArticle(long idArticle) {

		String trouverParArticle = "SELECT date_enchere, montant_enchere, id_utilisateur, id_article FROM Enchere WHERE id_article = :idArticle";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idArticle", idArticle);
        return namedParameterJdbcTemplate.query(trouverParArticle, parameterSource, new EnchereRowMapper());

	}

	@Override
	// consulter la liste des enchères
	public List<Enchere> consulterTout() {

		String consulTout = "SELECT id, date_enchere, montant_enchere, id_utilisateur, id_article FROM Enchere";

		return this.namedParameterJdbcTemplate.query(consulTout, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override
	// annulation d'une enchère
	public void annulerEnchere(Enchere enchere) {
		LocalDateTime dateEnchere = enchere.getDateEnchere();
		Long idUtilisateur = enchere.getUtilisateur().getId();

		String supprEnchere = "DELETE FROM Enchere WHERE date_enchere = :dateEnchere AND id_utilisateur = :idUtilisateur";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("date_enchere", dateEnchere);
		paramSource.addValue("id_utilisateur", idUtilisateur);

		namedParameterJdbcTemplate.update(supprEnchere, paramSource);
	}
	
	
	class EnchereRowMapper implements RowMapper<Enchere> {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere e = new Enchere();
            e.setMontantEnchere(rs.getInt("montant_enchere"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime dt = LocalDateTime.parse(rs.getString("date_enchere"),formatter);
            e.setDateEnchere(dt);
            Article a = new Article();
            a.setId(rs.getLong("id_article"));
            e.setArticle(null);
            Utilisateur u = new Utilisateur();
            u.setId(rs.getLong("id_utilisateur"));
            e.setUtilisateur(u);
          
            return e;
        }
    }

}
