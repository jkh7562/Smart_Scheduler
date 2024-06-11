module com.example.smart_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires okhttp3;
    requires com.google.gson;

    opens com.example.smart_scheduler to javafx.fxml;
    exports com.example.smart_scheduler;
}