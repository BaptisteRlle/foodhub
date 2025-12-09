package com.mycompany.projetrecette;

/**
 * Classe représentant un ingrédient dans le contexte d'une recette.
 * Contient le nom de l'ingrédient et sa quantité pour la recette.
 * 
 * @author bapti
 * @version 1.0
 */
public class IngredientRecette {

    private String nomIngredient; // nom de l'ingredient
    private String quantite; // ex: 250g, 1L, 2 cuilleres

    /**
     * Constructeur pour créer un ingrédient de recette.
     * 
     * @param nomIngredient le nom de l'ingrédient
     * @param quantite la quantité de l'ingrédient (ex: "250g", "1 cuillère à café")
     */
    public IngredientRecette(String nomIngredient, String quantite) {
        // on stocke le nom et la quantite pour chaque ingredient
        this.nomIngredient = nomIngredient;
        this.quantite = quantite;
    }

    /**
     * Retourne le nom de l'ingrédient.
     * 
     * @return le nom de l'ingrédient
     */
    public String getNomIngredient() {
        return nomIngredient; // retourne le nom de l'ingredient
    }

    /**
     * Retourne la quantité de l'ingrédient.
     * 
     * @return la quantité (avec unité de mesure)
     */
    public String getQuantite() {
        return quantite; // retourne la quantité (250g, 1L, etc)
    }
}
