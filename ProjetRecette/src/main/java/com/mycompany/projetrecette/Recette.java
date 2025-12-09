package com.mycompany.projetrecette;

/**
 * Classe représentant une recette avec tous ses attributs.
 * Contient les informations : identifiant, nom, genre, ingrédients, instructions, 
 * durée, nombre de personnes et prix moyen.
 * 
 * @author bapti
 * @version 1.0
 */
public class Recette {

    private int idRecette; // identifiant unique
    private String nomRecette; // nom de la recette
    private String genreRecette; // type: entree, plat, dessert, aperitif
    private String ingredients; // les ingredients (normalement une string)
    private String instructions; // comment preparer
    private String duree; // combien de temps ca prend
    private int nbPersonnes; // pr combien de personnes
    private double prixMoyen; // le prix moyen

    /**
     * Constructeur complet pour créer une recette avec tous ses paramètres.
     * 
     * @param idRecette l'identifiant unique de la recette
     * @param nomRecette le nom de la recette
     * @param genreRecette le genre de la recette (Entrée, Plat, Dessert, Apéritif)
     * @param ingredients la liste des ingrédients
     * @param instructions les instructions de préparation
     * @param duree la durée de préparation
     * @param nbPersonnes le nombre de personnes pour lequel la recette est prévue
     * @param prixMoyen le prix moyen de la recette
     */
    public Recette(int idRecette,
                   String nomRecette,
                   String genreRecette,
                   String ingredients,
                   String instructions,
                   String duree,
                   int nbPersonnes,
                   double prixMoyen) {
        // initialise tous les attributs de la recette
        this.idRecette = idRecette;
        this.nomRecette = nomRecette;
        this.genreRecette = genreRecette;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.duree = duree;
        this.nbPersonnes = nbPersonnes;
        this.prixMoyen = prixMoyen;   
    }

    /**
     * Retourne l'identifiant de la recette.
     * @return l'identifiant unique
     */
    public int getIdRecette() { return idRecette; } // retourne l'id unique
    
    /**
     * Retourne le nom de la recette.
     * @return le nom
     */
    public String getNomRecette() { return nomRecette; } // le nom de la recette
    
    /**
     * Retourne le genre de la recette.
     * @return le genre (Entrée, Plat, Dessert, Apéritif)
     */
    public String getGenreRecette() { return genreRecette; } // entree, plat, dessert, aperitif
    
    /**
     * Retourne la liste des ingrédients.
     * @return les ingrédients
     */
    public String getIngredients() { return ingredients; } // c'est une string normalement
    
    /**
     * Retourne les instructions de préparation.
     * @return les instructions
     */
    public String getInstructions() { return instructions; } // comment preparer
    
    /**
     * Retourne la durée de préparation.
     * @return la durée
     */
    public String getDuree() { return duree; } // combien de temps
    
    /**
     * Retourne le nombre de personnes.
     * @return le nombre de personnes
     */
    public int getNbPersonnes() { return nbPersonnes; } // pr combien de personnes
    
    /**
     * Retourne le prix moyen de la recette.
     * @return le prix moyen
     */
    public double getPrixMoyen() { return prixMoyen; } // le prix

    /**
     * Définit le nom de la recette.
     * @param n le nouveau nom
     */
    public void setNomRecette(String n) { this.nomRecette = n; } // setter pr le nom
    
    /**
     * Définit le genre de la recette.
     * @param g le nouveau genre
     */
    public void setGenreRecette(String g) { this.genreRecette = g; } // change le genre
    
    /**
     * Définit la liste des ingrédients.
     * @param i les nouveaux ingrédients
     */
    public void setIngredients(String i) { this.ingredients = i; } // met a jour les ingredients
    
    /**
     * Définit les instructions de préparation.
     * @param ins les nouvelles instructions
     */
    public void setInstructions(String ins) { this.instructions = ins; } // change les instructions
    
    /**
     * Définit la durée de préparation.
     * @param d la nouvelle durée
     */
    public void setDuree(String d) { this.duree = d; } // change la duree
    
    /**
     * Définit le nombre de personnes.
     * @param nb le nouveau nombre de personnes
     */
    public void setNbPersonnes(int nb) { this.nbPersonnes = nb; } // change le nb personnes
    
    /**
     * Définit le prix moyen de la recette.
     * @param p le nouveau prix moyen
     */
    public void setPrixMoyen(double p) { this.prixMoyen = p; } // setter pr le prix
}
