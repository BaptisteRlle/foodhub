module com.mycompany.projetrecette {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;

    opens com.mycompany.projetrecette;
    exports com.mycompany.projetrecette;
}
