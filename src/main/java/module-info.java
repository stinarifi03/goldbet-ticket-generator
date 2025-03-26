module com.example.goldbet {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.zxing;
    requires com.google.zxing.javase;


    opens com.example.goldbet to javafx.fxml;
    exports com.example.goldbet;
}