Projet Recette – README

Fonctionnalités de l’application

-   Ajouter, modifier, supprimer des recettes
-   Voir le contenu de la recette ( si on augmente le nombre de personne , les quantités augmentent
-   Gérer les ingrédients et quantités
-   Associer ingrédients <-> recettes via une table de liaison
-   Rechercher une recette et tri en fonction du Genre ou Nom ( ordre Alphabétique
-   Interface JavaFX simple en une seule fenêtre
-   Connexion à une base PostgreSQL

Comment utiliser l’application

1.  Lancer l’application (App.java)
2.  La fenêtre principale s’ouvre avec une table des recettes
3.  Pour ajouter une recette :
    -   Remplir : nom, genre, liste d’ingrédients, instructions obligatoirement ( et les autres sont optionels ) 
    -   Valider
4.  Pour modifier ou supprimer :
    -   Sélectionner une recette dans la liste
    -   Cliquer sur modifier / supprimer
5.  La BDD est mis à jour dès la moindre modification

Structure de la base de données

Tables principales : 
- recette(id, nom, genre, instructions, duree,
nb_personnes)
 - ingredient(id, nom) 
- recette_ingredient(id_recette,
id_ingredient, quantite)

