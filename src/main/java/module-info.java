module com.example.smart_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
<<<<<<< HEAD
    requires javafx.web;
=======
>>>>>>> 6463b142b88672096a85feb4ef572e0644dafbd0


    opens com.example.smart_scheduler to javafx.fxml;
    exports com.example.smart_scheduler;
}