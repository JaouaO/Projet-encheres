package fr.eni.encheres.dal;


import fr.eni.encheres.bo.Utilisateur;
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
                VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePpasse, :credit, :administrateur)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("pseudo", utilisateur.getPseudo());
        sqlParameterSource.addValue("nom", utilisateur.getNom());
        sqlParameterSource.addValue("prenom", utilisateur.getPrenom());
        sqlParameterSource.addValue("email", utilisateur.getEmail());
        sqlParameterSource.addValue("telephone", utilisateur.getTelephone());
        sqlParameterSource.addValue("rue", utilisateur.getRue());
        sqlParameterSource.addValue("codePostal", utilisateur.getCodePostal());
        sqlParameterSource.addValue("ville", utilisateur.getVille());
        sqlParameterSource.addValue("motDePasse", utilisateur.getMotDePasse());
        sqlParameterSource.addValue("credit", utilisateur.getCredit());
        sqlParameterSource.addValue("administrateur", utilisateur.isAdmin());

        namedParameterJdbcTemplate.update(ajouterUtilisateur, sqlParameterSource, keyHolder);

        if (keyHolder != null && keyHolder.getKey() != null) {
            utilisateur.setId(keyHolder.getKey().longValue());
        }

    }

    @Override
    public void modifierUtilisateur(Utilisateur utilisateur, long idUtilisateur) {
        if (idUtilisateur != 0) {
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
        MapSqlParameterSource ParameterSource = new MapSqlParameterSource();
        ParameterSource.addValue("id", idUtilisateur);

        namedParameterJdbcTemplate.update(modifUtilisateur, ParameterSource, keyHolder);
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
            MapSqlParameterSource ParameterSource = new MapSqlParameterSource();
            ParameterSource.addValue("id", idUtilisateur);
            namedParameterJdbcTemplate.update(supprUtilisateur, ParameterSource, keyHolder);
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
                WHERE id = :idUtilisateur"""
                ;
        MapSqlParameterSource ParameterSource = new MapSqlParameterSource();
        ParameterSource.addValue("id", idUtilisateur);
        return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(trouverParId, new UtilisateurRowMapper(), ParameterSource);
    }

    @Override
    public void ajouterCredits(int nbAjout, long idUtilisateur) {

        String creditPlus = """
                UPDATE Utilisateur 
                SET credit = credit+nbAjout
                WHERE id = :idUtilisateur
        """;

        MapSqlParameterSource ParameterSource = new MapSqlParameterSource();
        ParameterSource.addValue("id", idUtilisateur);

        namedParameterJdbcTemplate.update(creditPlus, ParameterSource);

    }

    @Override
    public void retirerCredits(int nbRetire, long idUtilisateur) {

        String CreditsMoins = """
                UPDATE Utilisateur 
                SET credit = credit-nbRetire
                WHERE id = :idUtilisateur
        """;

        MapSqlParameterSource ParameterSource = new MapSqlParameterSource();
        ParameterSource.addValue("id", idUtilisateur);

        namedParameterJdbcTemplate.update(CreditsMoins, ParameterSource);
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
            u.setMotDePasse(rs.getString("motDePasse"));
            u.setTelephone(rs.getString("telephone"));
            u.setRue(rs.getString("rue"));
            u.setCodePostal(rs.getString("codePostal"));
            u.setVille(rs.getString("ville"));
            u.setCredit(rs.getInt("credit"));
            u.setAdmin(rs.getBoolean("administrateur"));
            return u;
        }
    }
}

