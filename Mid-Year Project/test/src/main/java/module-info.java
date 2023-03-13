module midyearproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens midyearproject to javafx.fxml;
    exports midyearproject;
}