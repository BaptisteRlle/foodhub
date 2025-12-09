package com.mycompany.projetrecette;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la gestion des recettes en base de données.
 * Fournit les méthodes CRUD (Create, Read, Update, Delete) pour les recettes.
 * 
 * @author bapti
 * @version 1.0
 */
public class RecetteDAO {

    private Connection conn; // la connection a la bdd

    /**
     * Constructeur qui initialise la connexion à la base de données.
     */
    public RecetteDAO() {
        conn = Database.getInstance().getConnection(); // recupere la connection unique
    }

    /**
     * Récupère toutes les recettes de la base de données.
     * 
     * @return une liste contenant toutes les recettes
     */
    public List<Recette> getAll() {
        List<Recette> listeRecettes = new ArrayList<>();

        // requete pr recuperer tout depuis la table recettes
        String sql = "SELECT id, nom, genre, ingredients, instructions, duree, nb_personnes, prix_moyen " +
                     "FROM recettes";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            // boucle sur les resultats et crée les objets recette

            while (rs.next()) {
                Recette r = new Recette( // constructeur avec tous les params
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("genre"),
                        rs.getString("ingredients"),
                        rs.getString("instructions"),
                        rs.getString("duree"),
                        rs.getInt("nb_personnes"),
                        rs.getDouble("prix_moyen")
                );
                listeRecettes.add(r); // ajoute a la liste
            }

            rs.close(); // ferme le resultat
            pst.close(); // faut fermer les ressources

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listeRecettes;
    }

    /**
     * Insère une nouvelle recette dans la base de données.
     * 
     * @param recette la recette à insérer
     */
    public void insert(Recette recette) {

        // INSERT avec ? pr eviter les injections SQL
        String sql = "INSERT INTO recettes (nom, ingredients, genre, instructions, duree, nb_personnes, prix_moyen) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            // on met les parametres dans le bon ordre
            pst.setString(1, recette.getNomRecette()); // param 1: nom
            pst.setString(2, recette.getIngredients()); // param 2: ingredients
            pst.setString(3, recette.getGenreRecette()); // param 3: genre
            pst.setString(4, recette.getInstructions()); // param 4: instructions
            pst.setString(5, recette.getDuree()); // param 5: duree
            pst.setInt(6, recette.getNbPersonnes()); // param 6: nb personnes
            pst.setDouble(7, recette.getPrixMoyen()); // param 7: prix

            pst.executeUpdate(); // execute l'insert
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour une recette existante en base de données.
     * 
     * @param recette la recette avec les données mises à jour
     */
    public void update(Recette recette) {

        // UPDATE pr modifier une recette existante
        String sql = "UPDATE recettes SET nom = ?, ingredients = ?, genre = ?, instructions = ?, duree = ?, nb_personnes = ?, prix_moyen = ? " +
                     "WHERE id = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, recette.getNomRecette()); // le nouveau nom
            pst.setString(2, recette.getIngredients()); // les ingredients
            pst.setString(3, recette.getGenreRecette()); // le genre
            pst.setString(4, recette.getInstructions()); // les instructions
            pst.setString(5, recette.getDuree()); // la duree
            pst.setInt(6, recette.getNbPersonnes()); // le nb personnes
            pst.setDouble(7, recette.getPrixMoyen()); // le prix
            pst.setInt(8, recette.getIdRecette()); // WHERE id = ... c'est le 8eme param

            pst.executeUpdate(); // applique la modif
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime une recette de la base de données.
     * 
     * @param idRecette l'identifiant de la recette à supprimer
     */
    public void delete(int idRecette) {

        // DELETE la recette avec cet id
        String sql = "DELETE FROM recettes WHERE id = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idRecette);
            pst.executeUpdate();
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère l'identifiant de la dernière recette insérée.
     * 
     * @return l'identifiant de la dernière recette, ou -1 en cas d'erreur
     */
    public int getLastInsertedId() {

        // SELECT le max id (aka le dernier id inseree)
        String sql = "SELECT max(id) AS id FROM recettes";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                rs.close();
                pst.close();
                return id; // retourne l'id
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // erreur si -1
    }
}
