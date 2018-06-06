package gen.snakemulti;


import sun.security.provider.SecureRandom;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnexionSQL {
    private final static Logger LOG = Logger.getLogger(ConnexionSQL.class.getName());
    private  final String ADRESSE_DEFAUT = "localhost";
    private final int PORT_DEFAUT = 8889;
    private final String NOM_DB = "snakemulti";
    private String url;
    private final String user = "root";
    private final String pwd = "root";
    private final String nomTableUsers = "Users";
    private final String leaderBoard = "leaderboard";
    private Connection connexionSql;

    public ConnexionSQL(){
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
            url = "jdbc:mysql://" + ADRESSE_DEFAUT + ":" + PORT_DEFAUT + "/" + NOM_DB;
            System.out.println(url);
            LOG.log(Level.INFO, "Connexion à la base de données {0}...", NOM_DB);
            connexionSql = DriverManager.getConnection(url, user, pwd);
            LOG.log(Level.INFO, "Connexion réussie!");
        }catch(SQLException e){
            e.getStackTrace();
            LOG.log(Level.SEVERE, "Connexion échouée! " + e);
        }catch(ClassNotFoundException e){
            LOG.log(Level.SEVERE, "Driver non chargés!" + e);
        }
    }

    public ConnexionSQL(String adresse, int port){
        try {
            url = "jdbc:mysql://" + adresse + ":" + port + "/" + NOM_DB;
            LOG.log(Level.INFO, "Connexion à la base de données {0}...", NOM_DB);
            connexionSql = DriverManager.getConnection(url, user, pwd);
            LOG.log(Level.INFO, "Connexion réussie!");
        }catch(SQLException e){
            e.getStackTrace();
            LOG.log(Level.SEVERE, "Connexion échouée!");
        }
    }

    public boolean ajouterUser(String user, String pwd){
        try {
            Statement etat = connexionSql.createStatement();

            //Vérifier si le pseudo existe déjà
            if(verifierUser(user)) {
                String sel = toReadable(genererSel());
                String requete = "INSERT INTO " + nomTableUsers + " (username, password, salt) " +
                        "VALUES('" + user + "','" + hash(pwd) + "','" + sel + "')";
                etat.executeUpdate(requete);
                LOG.log(Level.INFO, "Le compte " + user + " a bien été créé");
                return true;
            }
            else{
                return false;
            }
        }catch(SQLException e){
            LOG.log(Level.SEVERE, "Erreur INSERT: Requête incorrecte!");
            return false;
        }
    }

    public boolean verifierConnexion(String user, String pwd) {
        try {
            Statement etat = connexionSql.createStatement();
            String requete = "SELECT * FROM " + nomTableUsers;

            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = etat.executeQuery(requete);

            //Passer en revue tous les pseudos déjà enregistrés
            while (result.next()) {
                String username = result.getString("username");
                String password = result.getString("password");

                //Si le pseudo existe déjà
                if(user.equals(username) && hash(pwd).equals((password))){
                    LOG.log(Level.WARNING, "Connexion réussie.");
                    return true;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erreur SELECT: Requête incorrecte!");
            return false;
        }
        LOG.log(Level.WARNING, "Connexion échouée.");
        return false;
    }

    public boolean verifierUser(String user) {
        try {
            Statement etat = connexionSql.createStatement();
            String requete = "SELECT * FROM " + nomTableUsers;

            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = etat.executeQuery(requete);

            //Passer en revue tous les pseudos déjà enregistrés
            while (result.next()) {
                String username = result.getString("username");

                //Si le pseudo existe déjà
                if(user.equals(username)){
                    LOG.log(Level.WARNING, "Le pseudo " + user + " est déjà utilisé.");
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erreur SELECT: Requête incorrecte!");
            return false;
        }
    }

    public boolean estConnexionValide(){
        boolean estValide = false;
        try {
            estValide = connexionSql.isClosed();
        }catch(SQLException e){
            e.getStackTrace();
        }
        return estValide;
    }

    static public byte[] genererSel() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.engineNextBytes(bytes);
        return bytes;
    }

    static private String toReadable(byte[] hash) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte byt : hash) {
            String hex = Integer.toHexString(byt);
            if (hex.length() == 1) {
                stringBuilder.append(0);
                stringBuilder.append(hex.charAt(hex.length() - 1));
            } else {
                stringBuilder.append(hex.substring(hex.length() - 2));
            }
        }
        return stringBuilder.toString();
    }

    private String hash(String text){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return DatatypeConverter.printHexBinary(digest.digest(text.getBytes(StandardCharsets.UTF_8))).toLowerCase();
        }catch(NoSuchAlgorithmException e){
            e.getStackTrace();
        }
        return "";
    }
    public int recupScore(int id_user) {
        try {
            Statement etat = connexionSql.createStatement();
            String requete = "SELECT * FROM " + leaderBoard;

            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = etat.executeQuery(requete);

            result.next();

            int game = result.getInt("user_id");
            return game;

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Erreur SELECT: Requête incorrecte!");
            return 2;
        }
    }
}