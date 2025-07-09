package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public EnchereDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void ajouterEnchere(Enchere enchere) {

		String creerEnchere = "INSERT INTO Enchere (date_enchere, montant_enchere, id_utilisateur, id_article) VALUES (:dateEnchere, :montantEnchere, :idUtilisateur, :idArticle)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

		parameterSource.addValue("dateEnchere", enchere.getDateEnchere());
        parameterSource.addValue("montantEnchere", enchere.getMontantEnchere());
		parameterSource.addValue("idUtilisateur", enchere.getUtilisateur().getId());
		parameterSource.addValue("idArticle", enchere.getArticle().getId());


        namedParameterJdbcTemplate.update(creerEnchere, parameterSource);
	}

	@Override
	public List<Enchere> consulterParUtilisateur(long idUtilisateur) {

		String trouverParUtilisateur = "SELECT date_enchere, montant_enchere, id_utilisateur, id_article FROM Enchere WHERE id_utilisateur = :idUtilisateur";
		
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idUtilisateur", idUtilisateur);

        return namedParameterJdbcTemplate.query(trouverParUtilisateur, parameterSource, new EnchereRowMapper());
	}

	@Override
	public List<Enchere> consulterParArticle(long idArticle) {

		String trouverParArticle = "SELECT date_enchere, montant_enchere, id_utilisateur, id_article FROM Enchere WHERE id_article = :idArticle";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idArticle", idArticle);
        return namedParameterJdbcTemplate.query(trouverParArticle, parameterSource, new EnchereRowMapper());

	}

	@Override
	public List<Enchere> consulterTout() {

		String consulTout = "SELECT id, date_enchere, montant_enchere, id_utilisateur, id_article FROM Enchere";

		return this.namedParameterJdbcTemplate.query(consulTout,new EnchereRowMapper());
	}

	@Override
		public void annulerEnchere(Enchere enchere) {
		LocalDateTime dateEnchere = enchere.getDateEnchere();
		Long idUtilisateur = enchere.getUtilisateur().getId();

		String supprEnchere = "DELETE FROM Enchere WHERE date_enchere = :dateEnchere AND id_utilisateur = :idUtilisateur";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("dateEnchere", dateEnchere);
		paramSource.addValue("idUtilisateur", idUtilisateur);

		namedParameterJdbcTemplate.update(supprEnchere, paramSource);
	}
	
	@Override
	public boolean hasEnchereUtilisateur(long idUtilisateur, long idArticle) {
		String compterEncheres = """
			SELECT COUNT(*) FROM Enchere e
					inner Join Article a ON a.id = e.id_article
					WHERE  a.id=:idArticle AND etat_vente = 'en_cours'  AND e.id_utilisateur=:idUtilisateur
				""";
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("idArticle", idArticle);
		parameterSource.addValue("idUtilisateur", idUtilisateur);

		Integer nbArticlesOuverts = namedParameterJdbcTemplate.queryForObject(compterEncheres, parameterSource, Integer.class);
		return nbArticlesOuverts !=0;
	}
	
	
	class EnchereRowMapper implements RowMapper<Enchere> {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere e = new Enchere();
            e.setMontantEnchere(rs.getInt("montant_enchere"));

//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//            LocalDateTime dt = LocalDateTime.parse(rs.getString("date_enchere"),formatter);
//            e.setDateEnchere(dt);
            e.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
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
