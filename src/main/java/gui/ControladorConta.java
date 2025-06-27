package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logica.ContextoAplicacao;
import logica.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ControladorConta
{
    @FXML
    private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private TextField campoTelefone;
    @FXML private PasswordField campoSenhaAtual;
    @FXML private PasswordField campoNovaSenha;
    @FXML private PasswordField campoConfirmarNovaSenha;
    @FXML private Label labelMensagem;

    private Usuario usuario;

    public void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
        campoNome.setText(usuario.getNome());
        campoEmail.setText(usuario.getEmail());
        campoTelefone.setText(usuario.getTelefone());
    }

    @FXML
    private void atualizarConta()
    {
        // Obtém valores dos campos da interface
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String telefone = campoTelefone.getText();
        String senhaAtual = campoSenhaAtual.getText();
        String novaSenha = campoNovaSenha.getText();
        String confirmarNovaSenha = campoConfirmarNovaSenha.getText();

        // Validação 1: Confirmação da senha atual
        if (!usuario.verificarSenha(senhaAtual)) {
            mostrarAlerta("Erro de Atualização", "Senha atual incorreta.");
            return;
        }

        // Validação 2: Consistência da nova senha (se preenchida)
        if (!novaSenha.isEmpty() && !novaSenha.equals(confirmarNovaSenha)) {
            mostrarAlerta("Erro de Atualização", "Nova senha e confirmação de senha não coincidem.");
            return;
        }

        // Atualização dos dados básicos
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        // Atualização condicional da senha (apenas se preenchida)
        if (!novaSenha.isEmpty()) {
            usuario.setSenha(novaSenha);
        }

        // Persistência das alterações
        ContextoAplicacao.getGerenciadorUsuarios().atualizarUsuario(usuario);
        labelMensagem.setText("Conta atualizada com sucesso!");
        limparCamposSenha();
    }

    @FXML
    private void excluirConta()
    {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Tem certeza que deseja excluir sua conta?");
        confirmacao.setContentText("Todos seus dados serão perdidos permanentemente.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Remove os arquivos de anotações e tarefas do usuário
            String email = usuario.getEmail();
            String prefixoArquivo = email.replace("@", "_").replace(".", "_");

            File arquivoAnotacoes = new File("dados/anotacoes_" + prefixoArquivo + ".json");
            File arquivoTarefas = new File("dados/tarefas_" + prefixoArquivo + ".json");

            if (arquivoAnotacoes.exists()) arquivoAnotacoes.delete();
            if (arquivoTarefas.exists()) arquivoTarefas.delete();

            // Remove o usuário do sistema
            ContextoAplicacao.getGerenciadorUsuarios().removerUsuario(email);
            ContextoAplicacao.getGerenciadorDados();

            // Redireciona para a tela de login
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
                Parent raiz = loader.load();

                Stage palco = (Stage) campoNome.getScene().getWindow();
                palco.setScene(new Scene(raiz));
                palco.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Não foi possível carregar a tela de login.");
            }
        }
    }

    private void limparCamposSenha() {
        campoSenhaAtual.clear();
        campoNovaSenha.clear();
        campoConfirmarNovaSenha.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}