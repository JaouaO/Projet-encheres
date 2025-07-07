package fr.eni.encheres.dal;


import fr.eni.encheres.bo.Utilisateur;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void creerUtilisateur(Utilisateur utilisateur) {
        String ajouterUtilisateur = """
                INSERT INTO Utilisateur (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur)
                VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :mot_de_passe, :credit, :administrateur)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("pseudo", utilisateur.getPseudo());
        sqlParameterSource.addValue("nom", utilisateur.getNom());
        sqlParameterSource.addValue("prenom", utilisateur.getPrenom());
        sqlParameterSource.addValue("email", utilisateur.getEmail());
        sqlParameterSource.addValue("telephone", utilisateur.getTelephone());
        sqlParameterSource.addValue("rue", utilisateur.getRue());
        sqlParameterSource.addValue("code_postal", utilisateur.getCodePostal());
        sqlParameterSource.addValue("ville", utilisateur.getVille());
        sqlParameterSource.addValue("mot_de_passe", utilisateur.getMotDePasse());
        sqlParameterSource.addValue("credit", utilisateur.getCredit());
        sqlParameterSource.addValue("administrateur", utilisateur.isAdmin());

        namedParameterJdbcTemplate.update(ajouterUtilisateur, sqlParameterSource, keyHolder);

        if (keyHolder != null && keyHolder.getKey() != null) {
            utilisateur.setId(keyHolder.getKey().longValue());
        }

    }

    @Override
    public void modifierUtilisateur(long idUtilisateur) {
        if (idUtilisateur != 0) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(idUtilisateur);

        String modifUtilisateur = """
                UPDATE Utilisateur
                SET pseudo = 'pseudo', 
                    nom = 'nom', 
                    prenom = 'prenom', 
                    email = 'email', 
                    telephone ='telephone', 
                    rue = 'rue', 
                    code_postal = 'codePostal', 
                    ville = 'ville', 
                    mot_de_passe = 'motDePasse', 
                    credit = 'credit', 
                    administrateur = 'administrateur'
                
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", idUtilisateur);

        namedParameterJdbcTemplate.update(modifUtilisateur, parameterSource, keyHolder);
        if (keyHolder != null && keyHolder.getKey() != null) {
            utilisateur.setId(keyHolder.getKey().longValue());
        }}
//ajouter message d'erreur en cas de Id non trouvé
    }

    @Override
    public void supprimerUtilisateur(long idUtilisateur) {
        if (idUtilisateur != 0) {

            String supprUtilisateur = """
                    DELETE FROM Utilisateur
                    WHERE id = :idUtilisateur
            """;
            KeyHolder keyHolder = new GeneratedKeyHolder();
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("id", idUtilisateur);
            namedParameterJdbcTemplate.update(supprUtilisateur, parameterSource, keyHolder);
        }

    }

    @Override
    public void desactiverUtilisateur(long idUtilisateur) {

    }

    @Override
    public Utilisateur consulterParId(long idUtilisateur) {
        String trouverParId = """
                SELECT id, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, administrateur
                FROM Utilisateur
                WHERE id = :idUtilisateur
        """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idUtilisateur", idUtilisateur);
        return namedParameterJdbcTemplate.queryForObject(trouverParId, parameterSource,  new BeanPropertyRowMapper<>(Utilisateur.class));
    }

    @Override
    public void ajouterCredits(int nbAjout, long idUtilisateur) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(idUtilisateur);

        int nouveauSolde = utilisateur.getCredit() + nbAjout;

        String creditPlus = """
                UPDATE Utilisateur 
                SET credit = :nouveauSolde
                WHERE id = :idUtilisateur
        """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idUtilisateur", idUtilisateur);
        parameterSource.addValue("nouveauSolde", nouveauSolde);

        namedParameterJdbcTemplate.update(creditPlus, parameterSource);

    }

    @Override
    public void retirerCredits(int nbRetire, long idUtilisateur) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(idUtilisateur);

        int nouveauSolde = utilisateur.getCredit() - nbRetire;

        String CreditsMoins = """
                UPDATE Utilisateur 
                SET credit = :nouveauSolde 
                WHERE id = :idUtilisateur
        """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idUtilisateur", idUtilisateur);
        parameterSource.addValue("nouveauSolde", nouveauSolde);

        namedParameterJdbcTemplate.update(CreditsMoins, parameterSource);
    }
    
    @Override
    public Utilisateur consulterParPseudo(String pseudo) {
        String SQL = "SELECT * FROM Utilisateur WHERE pseudo = :pseudo";
            return namedParameterJdbcTemplate.query(
                 SQL,
                 new MapSqlParameterSource("pseudo", pseudo),
                 new UtilisateurRowMapper()
         ).stream().findFirst().orElse(null);
}

    @Override
    public Utilisateur consulterParEmail(String email) {
        String SQL = "SELECT * FROM Utilisateur WHERE email = :email";
        return namedParameterJdbcTemplate.query(
                SQL,
                new MapSqlParameterSource("email", email),
                new UtilisateurRowMapper()
        ).stream().findFirst().orElse(null);
    }


    class UtilisateurRowMapper implements RowMapper<Utilisateur> {
        @Override
        public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
            Utilisateur u = new Utilisateur();
            u.setId(rs.getLong("id"));
            u.setPseudo(rs.getString("pseudo"));
            u.setPrenom(rs.getString("prenom"));
            u.setNom(rs.getString("nom"));
            u.setEmail(rs.getString("email"));
            u.setMotDePasse(rs.getString("mot_de_passe"));
            u.setTelephone(rs.getString("telephone"));
            u.setRue(rs.getString("rue"));
            u.setCodePostal(rs.getString("code_postal"));
            u.setVille(rs.getString("ville"));
            u.setCredit(rs.getInt("credit"));
            u.setAdmin(rs.getBoolean("administrateur"));
            return u;
        }
    }


}

