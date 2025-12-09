package com.mycompany.projetrecette;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data Access Object pour la gestion de la relation entre recettes et ingrédients.
 * Permet d'ajouter, modifier et supprimer les associations ingrédients-recettes.
 * 
 * @author bapti
 * @version 1.0
 */
public class RecetteIngredientDAO {

    private Connection conn; // la connection a la bdd

    /**
     * Constructeur qui initialise la connexion à la base de données.
     */
    public RecetteIngredientDAO() {
        conn = Database.getInstance().getConnection(); // recupere la connection du singleton
    }

    /**
     * Supprime tous les ingrédients associés à une recette.
     * 
     * @param idRecette l'identifiant de la recette dont supprimer les ingrédients
     */
    public void deleteAllForRecette(int idRecette) {
        try {
            String sql = "DELETE FROM recette_ingredients WHERE recette_id = ?"; // supprime tous les liens
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idRecette);
            pst.executeUpdate();
            // efface tous les liens entre cette recette et ses ingredients
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Associe un ingrédient à une recette avec une quantité spécifiée.
     * 
     * @param idRecette l'identifiant de la recette
     * @param idIngredient l'identifiant de l'ingrédient
     * @param quantite la quantité de l'ingrédient pour la recette (ex: "250g", "2 cuillères")
     */
    public void insert(int idRecette, int idIngredient, String quantite) {
        try {
            // INSERT pour ajouter l'association
            String sql = "INSERT INTO recette_ingredients(recette_id, ingredient_id, quantite) VALUES(?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, idRecette); // l'id de la recette
            pst.setInt(2, idIngredient); // l'id de l'ingredient
            pst.setString(3, quantite); // la quantite (250g, 1L, etc)

            pst.executeUpdate();
            pst.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
