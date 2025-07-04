
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
        String creerUnNouvelArticle = """
                INSERT INTO Article (id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, chemin_img)
                VALUES (:id, :nom, :description, :dateDebutEnchere, :dateFinEnchere, :miseAPrix, :prixVente, :etatVente, :idUtilisateur, :idCategorie, :cheminImg)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("nom",article.getNom());
        parameterSource.addValue("description", article.getDescription());
        parameterSource.addValue("dateDebutEnchere", article.getDateDebutEnchere());
        parameterSource.addValue("dateFinEnchere", article.getDateFinEnchere());
        parameterSource.addValue("miseAPrix", article.getMiseAPrix());
        parameterSource.addValue("prixVente", article.getPrixVente());
        parameterSource.addValue("etatVente", article.getEtatVente());
        parameterSource.addValue("idUtilisateur", article.getUtilisateur().getId());
        parameterSource.addValue("idCategorie",article.getCategorie().getId());
        parameterSource.addValue("cheminImg", article.getCheminImg());

        namedParameterJdbcTemplate.update(creerUnNouvelArticle, parameterSource, keyHolder);

        if (keyHolder.getKey() != null) {
            article.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public void mettreEnVente(long idArticle) {
        String mettreEtatEnVente = """
                UPDATE Article SET etat_vente = 'en_cours'
                WHERE id = :idArticle
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idArticle", idArticle);
        namedParameterJdbcTemplate.update(mettreEtatEnVente, parameterSource);
    }

    @Override
    public void annulerVente(long idArticle) {
        if (idArticle == 0) {
            String supprArticle = """
                DELETE Article
                WHERE id = :idArticle
                """;
            MapSqlParameterSource ParameterSource = new MapSqlParameterSource();
            ParameterSource.addValue("idArticle", idArticle);
            namedParameterJdbcTemplate.update(supprArticle, ParameterSource);
        }
    }

    @Override
    public void vendreArticle(long idArticle) {
        String mettreEtatVendu = """
                UPDATE Article SET etat_vente = 'terminee'
                WHERE id = :idArticle
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idArticle", idArticle);
        namedParameterJdbcTemplate.update(mettreEtatVendu, parameterSource);
    }

    @Override
    public Article consulterParId(long idArticle) {
        String trouverParId = """
                SELECT article.id, article.nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, chemin_img, pseudo, rue, code_postal, ville
                FROM Article
                INNER JOIN Utilisateur ON (Article.id_vendeur = utilisateur.id)
                WHERE article.id = :idArticle
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idArticle", idArticle);
        return namedParameterJdbcTemplate.queryForObject(trouverParId, parameterSource, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterTout() {
        String trouverTousLesArticles = """
                SELECT article.id, article.nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, chemin_img, pseudo, rue, code_postal, ville
                FROM Article
                INNER JOIN Utilisateur ON (Article.id_vendeur = utilisateur.id)
                """;
        return namedParameterJdbcTemplate.query(trouverTousLesArticles, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterParRecherche(String motRecherche) {
        String trouverParRecherche = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, chemin_img
                FROM article
                WHERE nom LIKE '%"+ motRecherche + "%' 
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("motRecherche", motRecherche);
        return namedParameterJdbcTemplate.query(trouverParRecherche, parameterSource, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterParCategorie(long idCategorie) {

        String trouverParCategorie = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, chemin_img
                FROM Article
                WHERE id_categorie = :idCategorie
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id_categorie", idCategorie);
        return namedParameterJdbcTemplate.query(trouverParCategorie, parameterSource, new ArticleRowMapper());
    }

    @Override
    public List<Article> consulterParEtat(String etatVente) {
        String trierParEtat = """
                SELECT id, nom, description, date_debut, date_fin , mise_a_prix , prix_vente ,etat_vente ,id_vendeur , id_categorie, chemin_img
                FROM Article
                WHERE etat_vente = :etatVente
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("etatVente", etatVente);
        return namedParameterJdbcTemplate.query(trierParEtat, parameterSource, new ArticleRowMapper());
    }

    class ArticleRowMapper implements RowMapper<Article> {
        @Override
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article a = new Article();
            a.setId(rs.getLong("id"));
            a.setNom(rs.getString("nom"));
            a.setDescription(rs.getString("description"));
            LocalDateTime dateDeb = rs.getObject("date_debut", LocalDate.class).atStartOfDay();
            a.setDateDebutEnchere(dateDeb);
            LocalDateTime dateFin = rs.getObject("date_fin", LocalDate.class).atStartOfDay();
            a.setDateFinEnchere(dateFin);
            a.setMiseAPrix(rs.getInt("mise_a_prix"));
            a.setPrixVente(rs.getInt("prix_vente"));
            a.setEtatVente(rs.getString("etat_vente"));
            Utilisateur u = new Utilisateur();
            u.setId(rs.getLong("id_vendeur"));
            a.setUtilisateur(u);
            u.setPseudo(rs.getString("pseudo"));
            u.setRue(rs.getString("rue"));
            u.setVille(rs.getString("ville"));
            u.setCodePostal(rs.getString("code_postal"));
            Categorie c = new Categorie();
            c.setId(rs.getLong("id_categorie"));
            a.setCategorie(c);
            a.setCheminImg(rs.getString("chemin_img"));

            return a;
        }
    }
}