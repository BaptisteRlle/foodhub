package com.mycompany.projetrecette;

/**
 * Classe utilitaire pour obtenir les informations sur le système.
 * Fournit des méthodes pour récupérer les versions de Java et JavaFX.
 * 
 * @author bapti
 * @version 1.0
 */
public class SystemInfo {

    /**
     * Retourne la version de Java installée.
     * 
     * @return la version de Java (ex: "17.0.1")
     */
    public static String javaVersion() {
        return System.getProperty("java.version"); // retourne la version
    }

    /**
     * Retourne la version de JavaFX utilisée.
     * 
     * @return la version de JavaFX
     */
    public static String javafxVersion() {
        return System.getProperty("javafx.version"); // retourne la versin de javafx
    }

}