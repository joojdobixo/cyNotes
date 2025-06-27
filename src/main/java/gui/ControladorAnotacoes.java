package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logica.Anotacao;
import persistencia.GerenciadorDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControladorAnotacoes
{
    @FXML private TextField campoTitulo; // Campo para título da anotação
    @FXML private TextArea campoConteudo; // Campo para conteúdo
    @FXML private ListView<Anotacao> listaAnotacoes;
    @FXML private Button btnSalvarAnotacao;
    @FXML private Button btnSalvarEdicaoAnotacao;

    private GerenciadorDados gerenciador;
    private String emailUsuario;
    private Anotacao anotacaoSendoEditada;

    @FXML
    private void initialize()
    {
        btnSalvarEdicaoAnotacao.setVisible(false); // Esconde o botão de salvar edição
        btnSalvarEdicaoAnotacao.setManaged(false); // Remove do layout



        /*
        obs Observable que monitora a propriedade
        oldSelection Anotação previamente selecionada (pode ser null)
        newSelection Nova anotação selecionada (pode ser null)
        */
        listaAnotacoes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            //Verifica se a seleção não é nula
            if (newSelection != null) {
                 //Se válida, chama o método para preencher os campos de edição om os dados da anotação selecionada
                preencherCamposParaEdicao(newSelection);
            }
        });
    }

    public void configurar(String emailUsuario, GerenciadorDados gerenciador)
    {
        // Recebe dados do usuário e carrega anotações
        this.emailUsuario = emailUsuario;
        this.gerenciador = gerenciador;
        carregarAnotacoes();
    }

    @FXML
    private void salvarAnotacao()
    {
        String titulo = campoTitulo.getText();
        String conteudo = campoConteudo.getText();

        // Valida se título foi preenchido
        if (titulo.isEmpty())
        {
            mostrarAlerta("Erro", "Título é obrigatório!");
            return;
        }
        // Cria e salva nova anotação

        List<Anotacao> anotacoes = new ArrayList<>(listaAnotacoes.getItems());
        Anotacao novaAnotacao = new Anotacao(titulo, conteudo);
        anotacoes.add(novaAnotacao);

        gerenciador.salvarAnotacoes(emailUsuario, anotacoes);
        listaAnotacoes.getItems().setAll(anotacoes); // Atualiza a lista

        mostrarAlerta("Sucesso", "Anotação Salva!");
        limparCampos();
    }

    @FXML
    private void editarAnotacao()
    {
        anotacaoSendoEditada = listaAnotacoes.getSelectionModel().getSelectedItem();
        if (anotacaoSendoEditada != null)
        {
            preencherCamposParaEdicao(anotacaoSendoEditada);
            // Prepara interface para edição
            btnSalvarAnotacao.setVisible(false);
            btnSalvarAnotacao.setManaged(false);
            btnSalvarEdicaoAnotacao.setVisible(true);
            btnSalvarEdicaoAnotacao.setManaged(true);
        }
    }

    @FXML
    private void salvarEdicaoAnotacao()
    {
        if (anotacaoSendoEditada != null) {
            anotacaoSendoEditada.setTitulo(campoTitulo.getText());
            anotacaoSendoEditada.setConteudo(campoConteudo.getText());

            // Salva alterações na anotação editada
            gerenciador.salvarAnotacoes(emailUsuario, listaAnotacoes.getItems());
            listaAnotacoes.refresh();
            limparCampos();
            anotacaoSendoEditada = null;
            btnSalvarAnotacao.setVisible(true);
            btnSalvarAnotacao.setManaged(true);
            btnSalvarEdicaoAnotacao.setVisible(false);
            btnSalvarEdicaoAnotacao.setManaged(false);
        }
    }

    private void preencherCamposParaEdicao(Anotacao anotacao) {
        campoTitulo.setText(anotacao.getTitulo());
        campoConteudo.setText(anotacao.getConteudo());
    }

    @FXML
    private void excluirAnotacao()
    {
        Anotacao anotacaoSelecionada = listaAnotacoes.getSelectionModel().getSelectedItem();
        if (anotacaoSelecionada != null)
        {
            // Mostra confirmação antes de excluir
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmação de Exclusão");
            confirmacao.setHeaderText("Tem certeza que deseja excluir esta anotação?");
            confirmacao.setContentText("Esta ação não poderá ser desfeita.");

            Optional<ButtonType> resultado = confirmacao.showAndWait();
            //Se clicar em OK, exclui a anotação
            if(resultado.isPresent() && resultado.get() == ButtonType.OK)
            {
                listaAnotacoes.getItems().remove(anotacaoSelecionada);
                gerenciador.salvarAnotacoes(emailUsuario, listaAnotacoes.getItems());
                limparCampos();
                anotacaoSendoEditada = null;
                btnSalvarAnotacao.setVisible(true);
                btnSalvarAnotacao.setManaged(true);
                btnSalvarEdicaoAnotacao.setVisible(false);
                btnSalvarEdicaoAnotacao.setManaged(false);
            }
        }
    }

    private void carregarAnotacoes()
    {
        // Carrega anotações do usuário
        listaAnotacoes.getItems().setAll(gerenciador.carregarAnotacoes(emailUsuario));
    }

    private void limparCampos()
    {
        // Reseta campos de texto
        campoTitulo.clear();
        campoConteudo.clear();
    }

    // Exibe popup de alerta
    private void mostrarAlerta(String titulo, String mensagem)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}