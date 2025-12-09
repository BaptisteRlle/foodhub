package com.mycompany.projetrecette;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;

/**
 * Application principale pour la gestion des recettes.
 * Fournit une interface utilisateur JavaFX pour créer, modifier, consulter et supprimer des recettes.
 * Permet également la recherche et le tri des recettes.
 * 
 * @author bapti
 * @version 1.0
 */
public class App extends Application {

    // les DAO pr acceder a la bdd
    private RecetteDAO recetteDAO = new RecetteDAO();
    private IngredientDAO ingredientDAO = new IngredientDAO();
    private RecetteIngredientDAO recetteIngredientDAO = new RecetteIngredientDAO();

    // la liste observable pr afficher dans la table
    private final ObservableList<Recette> recettes =
            FXCollections.observableArrayList();

    /**
     * Démarre l'application et initialise l'interface utilisateur.
     * Crée la table des recettes, les filtres de recherche, les boutons de tri et d'action.
     * 
     * @param stage la fenêtre principale de l'application
     */
    @Override
    public void start(Stage stage) {
        // charge les recettes depuis la base
        recettes.addAll(recetteDAO.getAll());

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 700, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // cree la table pr afficher les recettes
        TableView<Recette> table = new TableView<>();

        // barre de recherche
        TextField tfSearch = new TextField();
        tfSearch.setPromptText("Rechercher...");

        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Nom", "Genre", "Ingrédient");
        cbType.setValue("Nom"); // c'est par nom par defaut

        HBox hSearch = new HBox(10, tfSearch, cbType);

        // les boutons pr trier
        Button buttonTriNom = new Button("Trier par Nom");
        Button buttonTriGenre = new Button("Trier par Genre");

        HBox hTri = new HBox(10, buttonTriNom, buttonTriGenre);

        // on cree les colonnes de la table
        TableColumn<Recette, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomRecette"));

        TableColumn<Recette, String> colGenre = new TableColumn<>("Genre");
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genreRecette"));

        table.getColumns().addAll(colNom, colGenre);
        table.setItems(recettes); // charge les donnees dans la table

        borderPane.setCenter(table);

        // listener pr la recherche en temps reel
        tfSearch.textProperty().addListener((obs, oldV, newV) -> {
            appliquerFiltre(recettes, table, tfSearch.getText(), cbType.getValue());
            // applique le filtre a chaque fois qu'on tape quelquechose
        });

        // quand on change le type de recherche
        cbType.setOnAction(e -> {
            appliquerFiltre(recettes, table, tfSearch.getText(), cbType.getValue());
        });

        buttonTriNom.setOnAction(e -> {
            // trier par nom alphabetiquement cest easy
            FXCollections.sort(recettes,
                    (r1, r2) -> r1.getNomRecette().compareToIgnoreCase(r2.getNomRecette()));
        });

        buttonTriGenre.setOnAction(e -> {
            FXCollections.sort(recettes, // meme chose pr le genre
                    (r1, r2) -> r1.getGenreRecette().compareToIgnoreCase(r2.getGenreRecette()));
        });

        VBox topBox = new VBox(10, hSearch, hTri);
        borderPane.setTop(topBox);

        // les boutons d'action en bas
        HBox hbox = new HBox(8);

        Button buttonAdd = new Button("Ajouter");
        Button buttonMod = new Button("Modifier");
        Button buttonSup = new Button("Supprimer");
        Button buttonView = new Button("Voir");

        buttonAdd.setOnAction(e -> openEditor(null)); // null = nouvelle recette

        buttonMod.setOnAction(e -> {
            Recette selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) openEditor(selected);
        });

        buttonSup.setOnAction(e -> {
            Recette selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // d'abord on supprime les ingredients de la recette sinon ca va crash
                recetteIngredientDAO.deleteAllForRecette(selected.getIdRecette());
                recetteDAO.delete(selected.getIdRecette());
                recettes.clear();
                recettes.addAll(recetteDAO.getAll()); // recharge tout
            }
        });

        buttonView.setOnAction(e -> {
            Recette selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) openViewer(selected); // affiche les details
        });

        hbox.getChildren().addAll(buttonAdd, buttonMod, buttonSup, buttonView);
        borderPane.setBottom(hbox);
        


        

        stage.setTitle("Gestion de recettes");
        stage.setScene(scene);
        
        stage.setMinWidth(700);
        stage.setMinHeight(600);
        
        stage.show();
    }

    /**
     * Affiche une boîte de dialogue d'erreur avec le message spécifié.
     * 
     * @param message le message d'erreur à afficher
     */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // type erreur
        alert.setHeaderText(null); // pas de header
        alert.setContentText(message); // le msg a afficher
        alert.showAndWait(); // montre et attend
    }

    /**
     * Applique un filtre à la liste des recettes selon le texte et le type de recherche.
     * Les recherches peuvent se faire par nom, genre ou ingrédient.
     * 
     * @param recettes la liste complète des recettes
     * @param table le TableView à mettre à jour avec les résultats filtrés
     * @param texte le texte de recherche à appliquer
     * @param type le type de recherche : "Nom", "Genre" ou "Ingrédient"
     */
    private void appliquerFiltre(ObservableList<Recette> recettes,
                                 TableView<Recette> table,
                                 String texte,
                                 String type) {

        // si la recherche est vide on affiche tout
        if (texte == null || texte.isEmpty()) {
            table.setItems(recettes);
            return;
        }

        // cree une nouvelle liste avec que les recettes qui match
        ObservableList<Recette> filtre = FXCollections.observableArrayList();

        for (Recette r : recettes) {
            switch (type) {
                case "Nom":
                    // cherche dans le nom (insensible a la casse)
                    if (r.getNomRecette().toLowerCase().contains(texte.toLowerCase()))
                        filtre.add(r);
                    break;

                case "Genre":
                    // cherche dans le genre
                    if (r.getGenreRecette().toLowerCase().contains(texte.toLowerCase()))
                        filtre.add(r);
                    break;

                case "Ingrédient":
                    // la partie la plus compliquee, on cherche dans les ingrédients
                    List<IngredientRecette> ing = ingredientDAO.getPourRecette(r.getIdRecette());
                    for (IngredientRecette ir : ing) {
                        if (ir.getNomIngredient().toLowerCase().contains(texte.toLowerCase())) {
                            filtre.add(r);
                            break; // pas besoin de continuer si ya trouvé
                        }
                    }
                    break;
            }
        }

        table.setItems(filtre); // affiche les resultats filtres
    }
  

    /**
     * Ouvre la fenêtre d'édition pour créer ou modifier une recette.
     * Permet de saisir tous les détails de la recette : nom, genre, instructions, durée, 
     * nombre de personnes, prix moyen et ingrédients.
     * 
     * @param toEdit la recette à modifier, ou null pour créer une nouvelle recette
     */
    public void openEditor(Recette toEdit) {

        Stage dlg = new Stage();
        dlg.setTitle("Éditer recette");

        Label labelNom = new Label("Nom * :");
        TextField tfNom = new TextField();

        Label labelGenre = new Label("Genre * :");
        ComboBox<String> comboGenre = new ComboBox<>();
        comboGenre.getItems().addAll("Entrée", "Plat", "Dessert", "Apéritif");

        Label labelIns = new Label("Instructions * :");
        TextArea taIns = new TextArea();

        Label labelDuree = new Label("Durée :");
        TextField tfDuree = new TextField();

        Label labelNbPers = new Label("Nombre de personnes :");
        TextField tfNbPers = new TextField();
        tfNbPers.setEditable(false);
        tfNbPers.setMaxWidth(60);

        Button boutonMoins = new Button("-");
        Button boutonPlus = new Button("+");

        boutonMoins.setOnAction(ev -> {
            try {
                int nb = Integer.parseInt(tfNbPers.getText());
                if (nb > 1) tfNbPers.setText(String.valueOf(nb - 1));
            } 
            catch (Exception ex) {}
        });

        boutonPlus.setOnAction(ev -> {
            try {
                int nb = Integer.parseInt(tfNbPers.getText());
                tfNbPers.setText(String.valueOf(nb + 1));
            } 
            catch (Exception ex) {}
        });

        HBox hNb = new HBox(10);
        hNb.getChildren().addAll(boutonMoins, tfNbPers, boutonPlus);

        Label labelPrix = new Label("Prix moyen :");
        TextField tfPrix = new TextField();

        Label labelTableIng = new Label("Ingrédients * :");

        TableView<IngredientRecette> tableIng = new TableView<>();
        ObservableList<IngredientRecette> ingredientsListe = FXCollections.observableArrayList();

        TableColumn<IngredientRecette, String> colIngNom = new TableColumn<>("Nom");
        colIngNom.setCellValueFactory(new PropertyValueFactory<>("nomIngredient"));

        TableColumn<IngredientRecette, String> colIngQt = new TableColumn<>("Quantité");
        colIngQt.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        tableIng.setItems(ingredientsListe);
        tableIng.getColumns().addAll(colIngNom, colIngQt);

        TextField tfIngNom = new TextField();
        tfIngNom.setPromptText("Ingrédient");

        TextField tfIngQt = new TextField();
        tfIngQt.setPromptText("Quantité");

        Button btnAddIng = new Button("Ajouter ingrédient");
        btnAddIng.setOnAction(e -> {
            String nom = tfIngNom.getText().trim();
            String qt = tfIngQt.getText().trim();

            if (!nom.isEmpty() && !qt.isEmpty()) {
                // ajoute l'ingredient a la table
                ingredientsListe.add(new IngredientRecette(nom, qt));
                tfIngNom.clear();
                tfIngQt.clear();
            }
        });

        Button btnDelIng = new Button("Supprimer");
        btnDelIng.setOnAction(e -> {
            IngredientRecette sel = tableIng.getSelectionModel().getSelectedItem();
            if (sel != null) ingredientsListe.remove(sel);
        });

        HBox hIngButtons = new HBox(10, tfIngNom, tfIngQt, btnAddIng, btnDelIng);

        if (toEdit != null) {
            tfNom.setText(toEdit.getNomRecette());
            comboGenre.setValue(toEdit.getGenreRecette());
            taIns.setText(toEdit.getInstructions());
            tfDuree.setText(toEdit.getDuree());
            tfNbPers.setText(String.valueOf(toEdit.getNbPersonnes()));
            tfPrix.setText(String.valueOf(toEdit.getPrixMoyen()));

            List<IngredientRecette> ing = ingredientDAO.getPourRecette(toEdit.getIdRecette());
            ingredientsListe.addAll(ing);

        } else {
            tfNbPers.setText("1");
        }

        Button buttonOk = new Button("OK");
        Button cancelBtn = new Button("Annuler");

        cancelBtn.setOnAction(e -> dlg.close());

        buttonOk.setOnAction(e -> {

            try {
                String nom = tfNom.getText().trim();
                String genre = comboGenre.getValue();
                String instructions = taIns.getText().trim();
                String duree = tfDuree.getText().trim();
                double prix = tfPrix.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(tfPrix.getText());
                int nbPers = Integer.parseInt(tfNbPers.getText());

                if (nom.isEmpty() || genre == null || instructions.isEmpty() || ingredientsListe.isEmpty()) {
                    showError("Champs obligatoires non remplis.");
                    return;
                }

              
                Recette r;

                if (toEdit == null) {
                    // nouvelle recette
                    r = new Recette(0, nom, genre, "", instructions, duree, nbPers, prix);

                    recetteDAO.insert(r);
                    recettes.clear();
                    recettes.addAll(recetteDAO.getAll());

                    int idRecette = recetteDAO.getLastInsertedId();

                    // on ajoute chaque ingredient a la recette
                    for (IngredientRecette ir : ingredientsListe) {
                        int idIng = ingredientDAO.getIdByName(ir.getNomIngredient());
                        if (idIng == -1) idIng = ingredientDAO.insertIngredient(ir.getNomIngredient());

                        recetteIngredientDAO.insert(idRecette, idIng, ir.getQuantite());
                    }

                } else {
                    // modification d'une recete existante
                    toEdit.setNomRecette(nom);
                    toEdit.setGenreRecette(genre);
                    toEdit.setInstructions(instructions);
                    toEdit.setDuree(duree);
                    toEdit.setNbPersonnes(nbPers);
                    toEdit.setPrixMoyen(prix);

                    recetteDAO.update(toEdit);

                    // on efface les anciens ingredients pour en ajouter les nouveaux
                    recetteIngredientDAO.deleteAllForRecette(toEdit.getIdRecette());

                    for (IngredientRecette ir : ingredientsListe) {
                        int idIng = ingredientDAO.getIdByName(ir.getNomIngredient());
                        if (idIng == -1) idIng = ingredientDAO.insertIngredient(ir.getNomIngredient());

                        recetteIngredientDAO.insert(toEdit.getIdRecette(), idIng, ir.getQuantite());
                    }

                    recettes.clear();
                    recettes.addAll(recetteDAO.getAll());
                }

                dlg.close();

            } catch (Exception ex) {
                System.out.println("Erreur : " + ex.getMessage());
            }
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                labelNom, tfNom,
                labelGenre, comboGenre,
                labelIns, taIns,
                labelDuree, tfDuree,
                labelNbPers, hNb,
                labelPrix, tfPrix,
                labelTableIng, tableIng,
                hIngButtons,
                buttonOk, cancelBtn
        );

        Scene scene = new Scene(vbox, 500, 750);

        dlg.setScene(scene);
        
        dlg.setMinWidth(500);
        dlg.setMinHeight(750);

        dlg.show();
    }

    /**
     * Ouvre la fenêtre de visualisation pour afficher les détails complets d'une recette.
     * Affiche les ingrédients avec leurs quantités qui peuvent être ajustées selon le nombre de personnes.
     * 
     * @param recette la recette à afficher
     */
    public void openViewer(Recette recette) {
        Stage dlg = new Stage();
        dlg.setTitle("Recette");

        Label labelNom = new Label("Nom :");
        TextField tfNom = new TextField(recette.getNomRecette());
        tfNom.setEditable(false);

        Label labelGenre = new Label("Genre :");
        TextField tfGenre = new TextField(recette.getGenreRecette());
        tfGenre.setEditable(false);

        Label labelIns = new Label("Instructions :");
        TextArea taIns = new TextArea(recette.getInstructions());
        taIns.setEditable(false);

        Label labelDuree = new Label("Durée :");
        TextField tfDuree = new TextField(recette.getDuree());
        tfDuree.setEditable(false);

        Label labelNbPers = new Label("Personnes :");
        TextField tfNbPers = new TextField(String.valueOf(recette.getNbPersonnes()));
        tfNbPers.setEditable(false);

        Button btnMoinsView = new Button("-");
        Button btnPlusView = new Button("+");

        Label labelIng = new Label("Ingrédients :");
        TextArea taIng = new TextArea();
        taIng.setEditable(false);

        majQuantites(recette, recette.getNbPersonnes(), taIng);

        btnMoinsView.setOnAction(ev -> {
            try {
                int nb = Integer.parseInt(tfNbPers.getText());
                if (nb > 1) {
                    nb--;
                    tfNbPers.setText(String.valueOf(nb));
                    // recalcule les quantités
                    majQuantites(recette, nb, taIng);
                }
            } catch (Exception ex) {}
        });

        btnPlusView.setOnAction(ev -> {
            try {
                int nb = Integer.parseInt(tfNbPers.getText());
                nb++;
                tfNbPers.setText(String.valueOf(nb));
                majQuantites(recette, nb, taIng);
            } catch (Exception ex) {}
        });

        HBox hNbView = new HBox(10, btnMoinsView, tfNbPers, btnPlusView);

        Label labelPrix = new Label("Prix moyen :");
        TextField tfPrix = new TextField(String.valueOf(recette.getPrixMoyen()));
        tfPrix.setEditable(false);

        Button btnFermer = new Button("Fermer");
        btnFermer.setOnAction(e -> dlg.close());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                labelNom, tfNom,
                labelGenre, tfGenre,
                labelIns, taIns,
                labelDuree, tfDuree,
                labelNbPers, hNbView,
                labelIng, taIng,
                labelPrix, tfPrix,
                btnFermer
        );

        Scene scene = new Scene(vbox, 400, 700);
        
        dlg.setScene(scene);
        
        
        dlg.setMinWidth(400);
        dlg.setMinHeight(700);
        dlg.show();
    }

    /**
     * Met à jour les quantités des ingrédients en fonction du nombre de personnes affiché.
     * Recalcule les quantités en appliquant un ratio basé sur le nombre de personnes original.
     * 
     * @param recette la recette dont les quantités doivent être mises à jour
     * @param nbPersAffiche le nombre de personnes pour lequel calculer les quantités
     * @param taIng la TextArea où afficher les ingrédients avec les quantités mises à jour
     */
    private void majQuantites(Recette recette, int nbPersAffiche, TextArea taIng) {
        List<IngredientRecette> ing = ingredientDAO.getPourRecette(recette.getIdRecette());

        int nbOriginal = recette.getNbPersonnes();
        if (nbOriginal <= 0) nbOriginal = 1;

        // calcule le ratio pour adapter les quantites
        double ratio = (double) nbPersAffiche / (double) nbOriginal;

        String texte = "";
        for (IngredientRecette ir : ing) {

            String q = ir.getQuantite();
            if (q == null) q = "";

            // cherche ou commence le texte apres les chiffres
            int i = 0;
            while (i < q.length() && Character.isDigit(q.charAt(i))) {
                i++;
            }

            if (i == 0) {
                // pas de chiffre donc on laisse comme c'est
                texte += "- " + ir.getNomIngredient() + " : " + q + "\n";
            } else {
                String numPart = q.substring(0, i);
                String unitPart = q.substring(i);

                int val;
                try {
                    val = Integer.parseInt(numPart);
                } catch (Exception e) {
                    texte += "- " + ir.getNomIngredient() + " : " + q + "\n";
                    continue;
                }

                // multiplie la quantite par le ratio
                int newVal = (int) Math.round(val * ratio);
                texte += "- " + ir.getNomIngredient() + " : " + newVal + unitPart + "\n";
            }
        }

        taIng.setText(texte);
    }

    /**
     * Point d'entrée de l'application.
     * 
     * @param args les arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch();
    }
}