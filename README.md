# Projet Recette – Application JavaFX & PostgreSQL

Application de gestion de recettes réalisée dans le cadre de mon BUT Informatique.  
Elle permet d’ajouter, modifier, supprimer et consulter des recettes tout en gérant automatiquement les quantités en fonction du nombre de personnes.

---

## Fonctionnalités

- Ajouter, modifier et supprimer des recettes  
- Afficher le contenu complet d’une recette  
- Ajustement automatique des quantités lorsque le nombre de personnes change  
- Gestion des ingrédients et quantités associées  
- Association recettes ↔ ingrédients via une table de liaison  
- Recherche de recette  
- Tri par genre ou par ordre alphabétique  
- Interface JavaFX simple en une seule fenêtre  
- Connexion à une base de données PostgreSQL  
- Mise à jour instantanée de la BDD à chaque modification

---

## Utilisation

1. Lancer l’application via `App.java`  
2. La fenêtre principale s’affiche avec la liste des recettes  
3. Ajouter une recette :
   - Renseigner : nom, genre, ingrédients, instructions (obligatoires)
   - Les autres champs sont optionnels  
4. Modifier ou supprimer :
   - Sélectionner une recette dans la table
   - Cliquer sur Modifier ou Supprimer
5. Toutes les modifications sont directement enregistrées dans PostgreSQL

---

## Structure de la base de données

### Table `recette`
| Colonne       | Type     | Description                |
|---------------|----------|----------------------------|
| id            | SERIAL   | Identifiant unique         |
| nom           | VARCHAR  | Nom de la recette          |
| genre         | VARCHAR  | Catégorie / type           |
| instructions  | TEXT     | Étapes à suivre            |
| duree         | INT      | Durée estimée              |
| nb_personnes  | INT      | Nombre de personnes        |

### Table `ingredient`
| Colonne | Type     | Description         |
|---------|----------|---------------------|
| id      | SERIAL   | Identifiant unique  |
| nom     | VARCHAR  | Nom de l’ingrédient |

### Table `recette_ingredient`
| Colonne       | Type   | Description                       |
|---------------|--------|-----------------------------------|
| id_recette    | INT    | Référence vers `recette.id`        |
| id_ingredient | INT    | Référence vers `ingredient.id`     |
| quantite      | FLOAT  | Quantité utilisée                  |

---

## Technologies utilisées

- Java  
- JavaFX  
- PostgreSQL (AlwaysData)  
- JDBC  
- Design Pattern Singleton / DAO et Architecture Pattern MVC

---

## Objectif pédagogique

- Apprendre Java & JavaFX  
- Gérer une base de données relationnelle  
- Connecter Java et PostgreSQL  
- Manipuler les données avec JDBC  
- Suivre une architecture simple mais organisée

---

## Réalisation

Projet développé seul dans le cadre du BUT Informatique.

