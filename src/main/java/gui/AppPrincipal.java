package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppPrincipal extends Application
{
    @Override
    public void start(Stage palcoPrincipal) throws Exception
    {
        //Carrega a primeira Interface
        Parent raiz = FXMLLoader.load(getClass().getResource("/gui/Inicio.fxml"));

        new Image(getClass().getResourceAsStream("/CyNotes logo.png"));
        palcoPrincipal.setTitle("CyNotes - Organizador de Tarefas");
        palcoPrincipal.setScene(new Scene(raiz));
        palcoPrincipal.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}