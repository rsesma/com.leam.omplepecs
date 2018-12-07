package com.leam.omplepecs;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OmplePECs extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {        
        Parent root = FXMLLoader.load(getClass().getResource("OmplePECs.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Omple PEC");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
