package com.mycompany.projetrecette;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton pour gérer la connexion à la base de données PostgreSQL.
 * Établit une connexion unique à la base de données lors de la première utilisation.
 * 
 * @author bapti
 * @version 1.0
 */
public class Database {

    private static Database instance; // l'instance unique (singleton)
    private Connection connection; // la connection a la bdd

    // les infos pr la connection
    private final String url = "jdbc:postgresql://postgresql-projetrecette.alwaysdata.net:5432/projetrecette_recettemanager";
    private final String user = "projetrecette";  
    private final String password = "Bapt@13012!"; 

    /**
     * Constructeur privé pour implémenter le pattern singleton.
     * Établit la connexion à la base de données.
     */
    private Database() {
        try {
            // Tentative de connexion à la base
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion BDD OK");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion BDD");
            e.printStackTrace();
           
        }
    }


    /**
     * Obtient l'instance unique de la classe Database.
     * Crée l'instance lors du premier appel.
     * 
     * @return l'instance unique de Database
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database(); // cree une instance si existe pas
        }
        return instance;
    }

    /**
     * Retourne la connexion à la base de données.
     * 
     * @return la connexion JDBC
     */
    public Connection getConnection() {
        return connection; // retourne la connection actuelle
    }
    
    /**
     * Vérifie si la connexion à la base de données est active.
     * 
     * @return true si la connexion est établie, false sinon
     */
    public boolean isConnected() {
        return connection != null; // verifie si c'est pas null
    }

}
