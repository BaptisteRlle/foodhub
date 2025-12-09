package com.mycompany.projetrecette;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object pour la gestion des ingrédients en base de données.
 * Fournit les méthodes pour accéder et gérer les ingrédients.
 * 
 * @author bapti
 * @version 1.0
 */
public class IngredientDAO {

    private Connection conn; // la connection unique a la bdd

    /**
     * Constructeur qui initialise la connexion à la base de données.
     */
    public IngredientDAO() {
        conn = Database.getInstance().getConnection(); // recup la connection
    }

    /**
     * Récupère l'identifiant d'un ingrédient par son nom.
     * 
     * @param nom le nom de l'ingrédient à rechercher
     * @return l'identifiant de l'ingrédient, ou -1 si non trouvé
     */
    public int getIdByName(String nom) {
        try {
            String sql = "SELECT id FROM ingredients WHERE nom = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nom);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                rs.close();
                pst.close();
                return id;
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Insère un nouvel ingrédient dans la base de données.
     * 
     * @param nom le nom de l'ingrédient à insérer
     * @return l'identifiant de l'ingrédient inséré, ou -1 en cas d'erreur
     */
    public int insertIngredient(String nom) {
        try {
            String sql = "INSERT INTO ingredients(nom) VALUES(?) RETURNING id";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nom);
            // RETURNING id cest pour avoir l'id juste apres l'insertion
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(1);
                rs.close();
                pst.close();
                return id;
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Récupère la liste des ingrédients pour une recette donnée.
     * 
     * @param idRecette l'identifiant de la recette
     * @return une liste d'IngredientRecette contenant les ingrédients et leurs quantités
     */
    public List<IngredientRecette> getPourRecette(int idRecette) {

        List<IngredientRecette> liste = new ArrayList<>();

        // join pr avoir les noms des ingredients et pas juste les ids
        String sql = "SELECT i.nom, ri.quantite " +
                     "FROM recette_ingredients ri " +
                     "JOIN ingredients i ON ri.ingredient_id = i.id " +
                     "WHERE ri.recette_id = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idRecette); // l'id de la recette
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom"); // le nom depuis la table ingredients
                String quantite = rs.getString("quantite"); // ex: "250g"
                IngredientRecette ir = new IngredientRecette(nom, quantite); // cree l'objet
                liste.add(ir); // ajoute a la liste
            }

            rs.close(); // toujours fermer
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }
}
