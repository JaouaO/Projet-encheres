
package fr.eni.encheres.dal;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void creerArticle(Article article) {
        String ADD_ARTICLE = """
                INSERT INTO Article (id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie)
                VALUES (:id, :nom, :description, :dateDebutEnchere, :dateFinEnchere, :miseAPrix, :prixVente, :etatVente, :utilisateur, :categorie)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", article.getId());
        parameterSource.addValue("nom", article.getNom());
        parameterSource.addValue("description", article.getDescription());
        parameterSource.addValue("date_debut", article.getDateDebutEnchere());
        parameterSource.addValue("date_fin", article.getDateFinEnchere());
        parameterSource.addValue("miseAPrix", article.getMiseAPrix());
        parameterSource.addValue("prix_vente", article.getPrixVente());
        parameterSource.addValue("etatVente", article.getEtatVente());
        parameterSource.addValue("utilisateur", article.getUtilisateur());
        parameterSource.addValue("categorie",article.getCategorie());

        namedParameterJdbcTemplate.update(ADD_ARTICLE, parameterSource, keyHolder);

        if (keyHolder.getKey() != null) {
            article.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void mettreEnVente(long idArticle) {
        String nvEtatVente = "en_cours";
        String UPDATE_ETAT = """
                UPDATE Article SET etat_vente = nvEtatVente
                WHERE id = :idArticle
                """;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", idArticle);
        namedParameterJdbcTemplate.update(nvEtatVente, sqlParameterSource);
    }

    @Override
    public void annulerVente(long idArticle) {
        if (idArticle == 0) {
            String REMOVE_ARTICLE = """
                DELETE Article
                WHERE id = :idArticle
                """;
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
            sqlParameterSource.addValue("id", idArticle);
            namedParameterJdbcTemplate.update(REMOVE_ARTICLE, sqlParameterSource);
        }
    }

    @Override
    public void vendreArticle(long idArticle) {
        String nvEtatVente = "terminee";
        String UPDATE_ETAT = """
                UPDATE Article SET etat_vente = nvEtatVente
                WHERE id = :idArticle
                """;
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("id", idArticle);
        namedParameterJdbcTemplate.update(nvEtatVente, sqlParameterSource);
    }

    @Override
    public Article consulterparId(long idArticle) {
        String FIND_BY_ID = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, libelle
                FROM Article
                WHERE idArticle = :idArticle
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idArticle);
        return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, parameterSource, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterTout() {
        String GET_ALL = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, libelle
                FROM article
                """;
        return namedParameterJdbcTemplate.query(GET_ALL, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterParRecherche(String motRecherche) {
        String FIND_BY_SEARCH = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, libelle
                FROM article AS a
                INNER JOIN categorie AS c ON (a.id_categorie=c.id)
                WHERE nom LIKE "%" + motRecherche + "%"
                OR libelle LIKE "%" + motRecherche + "%"
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("nom", motRecherche);
        parameterSource.addValue("libelle", motRecherche);
        return namedParameterJdbcTemplate.query(FIND_BY_SEARCH, parameterSource, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterParCategorie(Categorie categorie) {

        String FIND_BY_CATEGORIE = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie
                FROM Article
                WHERE categorie = :categorie
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("categorie", categorie);
        return namedParameterJdbcTemplate.query(FIND_BY_CATEGORIE, parameterSource, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterParEtat(String etatVente) {
        String FIND_BY_ETAT = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie
                FROM Article
                WHERE etat_vente = :etatVente
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("etatVente", etatVente);
        return namedParameterJdbcTemplate.query(FIND_BY_ETAT, parameterSource, new ArticleRowMapper());
    }

    class ArticleRowMapper implements RowMapper<Article> {
        @Override
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article a = new Article();
            a.setId(rs.getLong("id"));
            a.setNom(rs.getString("nom"));
            a.setDescription(rs.getString("description"));
            LocalDateTime dateDeb = rs.getObject("dateDebutEnchere", LocalDate.class).atStartOfDay();
            a.setDateDebutEnchere(dateDeb);
            LocalDateTime dateFin = rs.getObject("dateFinEnchere", LocalDate.class).atStartOfDay();
            a.setDateFinEnchere(dateFin);
            a.setMiseAPrix(rs.getInt("miseAPrix"));
            a.setPrixVente(rs.getInt("prixVente"));
            a.setEtatVente(rs.getString("etatVente"));
            a.setUtilisateur((Utilisateur) rs.getObject("utlisateur"));
            a.setCategorie((Categorie) rs.getObject("categorie"));

            return a;
        }
    }
}