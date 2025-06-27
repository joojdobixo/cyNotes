package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControladorInicial
{
    @FXML
    private void abrirLogin()
    {
        try
        {
            Parent raiz = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
            Stage palco = new Stage();
            palco.setScene(new Scene(raiz));
            palco.setTitle("Login");
            palco.show();

            // Fecha a tela inicial
            //((Stage) raiz.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirCadastro()
    {
        try
        {
            Parent raiz = FXMLLoader.load(getClass().getResource("/gui/Cadastro.fxml"));
            Stage palco = new Stage();
            palco.setScene(new Scene(raiz));
            palco.setTitle("Cadastro");
            palco.show();

            // Fecha a tela inicial
            // ((Stage) raiz.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}