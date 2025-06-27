package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.ContextoAplicacao;
import logica.GerenciadorUsuarios;
import logica.Usuario;
import java.io.IOException;

public class ControladorLogin
{
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private Button botaoLogin;
    @FXML private Button botaoCadastro;
    @FXML private Label labelMensagem;

    private final GerenciadorUsuarios gerenciadorUsuarios = ContextoAplicacao.getGerenciadorUsuarios();

    //metodo para debug
    @FXML
    public void initialize()
    {
        System.out.println("[DEBUG] CampoEmail injetado? " + (campoEmail != null));
        System.out.println("[DEBUG] CampoSenha injetado? " + (campoSenha != null));
        System.out.println("[DEBUG] Botão Login injetado? " + (botaoLogin != null));
        System.out.println("[DEBUG] Botão Cadastro injetado? " + (botaoCadastro != null));
    }

    @FXML
    private void fazerLogin()
    {
        // Autentica usuário
        if (campoEmail == null || campoSenha == null)
        {
            System.err.println("ERRO: Campos de email/senha não foram injetados!");
            labelMensagem.setText("Erro interno no sistema. Reinicie o aplicativo.");
            return;
        }

        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText().trim();

        if(email.isEmpty() || senha.isEmpty())
        {
            labelMensagem.setText("Preencha todos os campos!");
            return;
        }

        try
        {
            if(gerenciadorUsuarios.fazerLogin(email, senha))
            {
                carregarTelaPrincipal();
            } else {
                labelMensagem.setText("ATENÇÃO: Email ou senha incorretos!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            labelMensagem.setText("Erro durante o login. Tente novamente.");
        }
    }

    private void carregarTelaPrincipal() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Principal.fxml"));
        Parent raiz = loader.load();

        ControladorPrincipal controlador = loader.getController();
        // Abre tela principal passando usuário logado
        controlador.setUsuarioLogado(gerenciadorUsuarios.getUsuarioLogado());

        Stage palco = (Stage) botaoLogin.getScene().getWindow();
        palco.setScene(new Scene(raiz));
        palco.show();
    }

    @FXML
    private void abrirTelaCadastro()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cadastro.fxml"));
            // Navega para tela de cadastro
            Parent raiz = loader.load();

            Stage palco = (Stage) botaoCadastro.getScene().getWindow();
            palco.setScene(new Scene(raiz));
            palco.show();
        } catch (Exception e) {
            e.printStackTrace();
            labelMensagem.setText("Erro ao abrir tela de cadastro");
        }
    }
}