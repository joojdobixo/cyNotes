package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.ContextoAplicacao;
import logica.GerenciadorUsuarios;
import persistencia.GerenciadorDados;

public class ControladorCadastro
{
    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private TextField campoTelefone;
    @FXML private Button botaoCadastrar;
    @FXML private Label labelMensagem;

    private final GerenciadorDados gerenciadorDados = ContextoAplicacao.getGerenciadorDados();
    private final GerenciadorUsuarios gerenciadorUsuarios = ContextoAplicacao.getGerenciadorUsuarios();

    @FXML
    private void cadastrarUsuario()
    {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = campoSenha.getText();
        String telefone = campoTelefone.getText();

        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty())
        {
            labelMensagem.setText("Nome, email e senha são obrigatórios!!");
            return;
        }

        if(gerenciadorUsuarios.cadastrarUsuario(nome, email, senha, telefone))
        {
            gerenciadorDados.salvarUsuarios(gerenciadorUsuarios.getUsuarios());
            //popup usuario cadastrado com sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cadastro realizado");
            alert.setHeaderText(null);
            alert.setContentText("Usuário cadastrado com sucesso!");
            alert.showAndWait();
            voltarParaLogin();
        } else{
            labelMensagem.setText("Email já cadastrado!");
        }
    }

    @FXML
    private void voltarParaLogin()
    {
        try
        {
            Parent raiz = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
            Stage palco = (Stage) botaoCadastrar.getScene().getWindow();
            palco.setScene(new Scene(raiz));
            palco.show();
        } catch(Exception e){
            e.printStackTrace();
            labelMensagem.setText("Erro ao carregar a tela de login :(");
        }
    }
}