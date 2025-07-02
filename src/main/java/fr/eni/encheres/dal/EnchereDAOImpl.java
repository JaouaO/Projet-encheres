package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Enchere;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.LocalDateTime;
import java.util.List;

public class EnchereDAOImpl implements EnchereDAO {

	private JdbcTemplate namedParameterJdbcTemplate;

	@Override

	// ajout d'une enchère
	public List<Enchere> ajouterEnchere(Enchere enchere) {

		String creerEnchere = "INSERT INTO Enchere (id, date_enchere, montant_enchere, id_utilisateur, id_article) VALUES (:id, :dateEnchere, :montantEnchere, :idUtilisateur, :idArticle)";

		return this.namedParameterJdbcTemplate.query(creerEnchere, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override

	// consultation de l'utilisateur par son id
	public List<Enchere> consulterParUtilisateur(long idUtilisateur) {

		String trouverUtilisateur = "SELECT id_utilisateur FROM Enchere WHERE id_utilisateur = :idUtilisateur";

		return this.namedParameterJdbcTemplate.query(trouverUtilisateur, new BeanPropertyRowMapper<>(Enchere.class));

	}

	@Override
	// consultation par article
	public List<Enchere> consulterParArticle(long idArticle) {

		String trouverParArticle = "SELECT id_article FROM Enchere WHERE id_article = :idArticle";

		return this.namedParameterJdbcTemplate.query(trouverParArticle, new BeanPropertyRowMapper<>(Enchere.class));

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

}
