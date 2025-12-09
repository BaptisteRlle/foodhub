package com.mycompany.projetrecette;

/**
 * Classe représentant un ingrédient.
 * Chaque ingrédient a un identifiant unique et un nom.
 * 
 * @author bapti
 * @version 1.0
 */
public class Ingredient {
    private int id; // identifiant de l'ingredient
    private String nom; // nom de l'ingredient
    
    /**
     * Constructeur par défaut pour la classe Ingredient.
     */
    public Ingredient() {
        // constructeur vide
    }
    
    /**
     * Constructeur avec paramètres.
     * 
     * @param id l'identifiant unique de l'ingrédient
     * @param nom le nom de l'ingrédient
     */
    public Ingredient(int id, String nom) {
        // simple constructeur pour les ingredients
        this.id = id;
        this.nom = nom;
    }
    
    /**
     * Retourne l'identifiant de l'ingrédient.
     * @return l'identifiant
     */
    public int getId() {
        return id; // juste l'id
    }
    
    /**
     * Définit l'identifiant de l'ingrédient.
     * @param id le nouvel identifiant
     */
    public void setId(int id) {
        this.id = id; // change l'id
    }
    
    /**
     * Retourne le nom de l'ingrédient.
     * @return le nom
     */
    public String getNom() {
        return nom; // retourne le nom de l'ingredient
    }
    
    /**
     * Définit le nom de l'ingrédient.
     * @param nom le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom; // change le nom
    }
}
