module com.example.smart_scheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.smart_scheduler to javafx.fxml;
    exports com.example.smart_scheduler;
}