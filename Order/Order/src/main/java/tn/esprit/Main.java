package tn.esprit;

import tn.esprit.utils.DataBase;

/*
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        DataBase.initTables();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn.esprit.views/user.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        primaryStage.setTitle("Gestion Utilisateurs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // ✅ Appel à la méthode launch() de Application
    }
}*/


public class Main {
    public static void main(String[] args) {
        DataBase.initTables();
        System.out.println("Database initialized successfully!");
        try {
            System.out.println("Container is running...");
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Container interrupted, shutting down.");
            Thread.currentThread().interrupt();
        }
    }
}
