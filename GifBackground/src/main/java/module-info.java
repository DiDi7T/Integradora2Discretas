module org.icesi.gifbackground {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.icesi.gifbackground to javafx.fxml;

    exports org.icesi.gifbackground.model;
    opens org.icesi.gifbackground.model to javafx.fxml;
    exports org.icesi.gifbackground.ui;
    opens org.icesi.gifbackground.ui to javafx.fxml;
}