package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import logica.ContextoAplicacao;
import logica.GerenciadorUsuarios;
import logica.Tarefa;
import logica.Usuario;
import persistencia.GerenciadorDados;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ControladorPrincipal
{
    @FXML private ListView<Tarefa> listaTarefas;
    @FXML private TextField campoTituloTarefa;
    @FXML private TextArea campoDescricaoTarefa;
    @FXML private DatePicker campoDataVencimento;
    @FXML private DatePicker dataLembrete;
    @FXML private Spinner<LocalTime> horaLembrete;
    @FXML private VBox painelPrincial;
    @FXML private Label labelUsuarioLogado;
    @FXML private Button btnAdicionarTarefa;
    @FXML private Button btnSalvarEdicaoTarefa;

    private Usuario usuarioLogado;
    private GerenciadorDados gerenciadorDados = ContextoAplicacao.getGerenciadorDados();
    private Tarefa tarefaSendoEditada;

    @FXML
    private void initialize()
    {
        configurarSpinnerHora();
        btnSalvarEdicaoTarefa.setVisible(false);
        btnSalvarEdicaoTarefa.setManaged(false);

        listaTarefas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposParaEdicao(newSelection);
            }
        });
    }

    public void setUsuarioLogado(Usuario usuario)
    {
        this.usuarioLogado = usuario;
        atualizarInterfaceUsuario();

        List<Tarefa> tarefas = gerenciadorDados.carregarTarefas(usuario.getEmail());
        listaTarefas.getItems().setAll(tarefas);
    }

    private void atualizarInterfaceUsuario()
    {
        if(usuarioLogado != null)
        {
            labelUsuarioLogado.setText("Bem-vindo(a), " + usuarioLogado.getNome());
        }
    }

    //Configura o componente Spinner para seleção de horário com incrementos de 30 minutos
    private void configurarSpinnerHora()
    {
        SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<LocalTime>()
        {
            @Override
            public void decrement(int steps)
            {
                // Decrementa o valor atual em passos de 30 minutos
                setValue(getValue().minusMinutes(30 * steps));

            }
            // Incrementa o valor atual em passos de 30 minutos
            @Override
            public void increment(int steps)
            {
                setValue(getValue().plusMinutes(30 * steps));
            }
        };
        // valor padrão 12:0h
        factory.setValue(LocalTime.of(12, 0));
        // Define o formato de exibição como HH:mm (24 horas)
        factory.setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm")));
        factory.setWrapAround(true); // Permite rolagem cíclica (23:59 -> 00:00)
        horaLembrete.setValueFactory(factory); // Aplica a configuração ao Spinner
    }

    @FXML
    private void adicionarTarefa()
    {
        String titulo = campoTituloTarefa.getText();
        LocalDate vencimento = campoDataVencimento.getValue();

        if(titulo.isEmpty())
        {
            mostrarAlerta("Erro", "Título é obrigatório!");
            return;
        }

        LocalDateTime lembrete = null;
        if(dataLembrete.getValue() != null && horaLembrete.getValue() != null)
        {
            //recebe data
            lembrete = LocalDateTime.of(dataLembrete.getValue(), horaLembrete.getValue());
        }
        // Cria nova tarefa e atualiza lista
        Tarefa novaTarefa = new Tarefa(titulo, campoDescricaoTarefa.getText(), vencimento, lembrete);
        listaTarefas.getItems().add(novaTarefa);
        gerenciadorDados.salvarTarefas(usuarioLogado.getEmail(), listaTarefas.getItems());
        limparCamposTarefa();
    }

    @FXML
    private void editarTarefa()
    {
        tarefaSendoEditada = listaTarefas.getSelectionModel().getSelectedItem();
        if(tarefaSendoEditada != null)
        {
            preencherCamposParaEdicao(tarefaSendoEditada);
            btnAdicionarTarefa.setVisible(false);
            btnAdicionarTarefa.setManaged(false);
            btnSalvarEdicaoTarefa.setVisible(true);
            btnSalvarEdicaoTarefa.setManaged(true);
        }
    }

    @FXML
    private void salvarEdicaoTarefa()
    {
        if (tarefaSendoEditada != null) {
            tarefaSendoEditada.setTitulo(campoTituloTarefa.getText());
            tarefaSendoEditada.setDescricao(campoDescricaoTarefa.getText());
            tarefaSendoEditada.setDataVencimento(campoDataVencimento.getValue());

            LocalDateTime lembrete = null;
            if(dataLembrete.getValue() != null && horaLembrete.getValue() != null)
            {
                lembrete = LocalDateTime.of(dataLembrete.getValue(), horaLembrete.getValue());
            }
            tarefaSendoEditada.setLembrete(lembrete);

            gerenciadorDados.salvarTarefas(usuarioLogado.getEmail(), listaTarefas.getItems());
            listaTarefas.refresh();
            limparCamposTarefa();
            tarefaSendoEditada = null;
            btnAdicionarTarefa.setVisible(true);
            btnAdicionarTarefa.setManaged(true);
            btnSalvarEdicaoTarefa.setVisible(false);
            btnSalvarEdicaoTarefa.setManaged(false);
        }
    }
//Preenche os campos da interface com os dados da tarefa selecionada para edição
    private void preencherCamposParaEdicao(Tarefa tarefa) {
        // Preenche os campos básicos
        campoTituloTarefa.setText(tarefa.getTitulo());
        campoDescricaoTarefa.setText(tarefa.getDescricao());
        // Configura a data de vencimento
        campoDataVencimento.setValue(tarefa.getDataVencimento());
        // Configura o lembrete (se existir)
        if (tarefa.getLembrete() != null) {
            // Preenche data e hora do lembrete
            dataLembrete.setValue(tarefa.getLembrete().toLocalDate());
            horaLembrete.getValueFactory().setValue(tarefa.getLembrete().toLocalTime());

        } else {
            // Reseta os campos de lembrete se não houver
            dataLembrete.setValue(null);
            horaLembrete.getValueFactory().setValue(LocalTime.of(12, 0));
        }
    }

    @FXML
    private void excluirTarefa()
    {
        Tarefa tarefaSelecionada = listaTarefas.getSelectionModel().getSelectedItem();
        if(tarefaSelecionada != null)
        {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmação de Exclusão");
            confirmacao.setHeaderText("Tem certeza que deseja excluir esta tarefa?");
            confirmacao.setContentText("Esta ação não poderá ser desfeita.");

            Optional<ButtonType> resultado = confirmacao.showAndWait();
            if(resultado.isPresent() && resultado.get() == ButtonType.OK)
            {
                listaTarefas.getItems().remove(tarefaSelecionada);
                gerenciadorDados.salvarTarefas(usuarioLogado.getEmail(), listaTarefas.getItems());
                limparCamposTarefa();
                tarefaSendoEditada = null;
                btnAdicionarTarefa.setVisible(true);
                btnAdicionarTarefa.setManaged(true);
                btnSalvarEdicaoTarefa.setVisible(false);
                btnSalvarEdicaoTarefa.setManaged(false);
            }
        }
    }

    @FXML
    private void marcarDesmarcarTarefa()
    {
        Tarefa tarefaSelecionada = listaTarefas.getSelectionModel().getSelectedItem();
        if(tarefaSelecionada != null)
        {
            // Alterna status de conclusão
            tarefaSelecionada.setConcluida(!tarefaSelecionada.isConcluida());
            gerenciadorDados.salvarTarefas(usuarioLogado.getEmail(), listaTarefas.getItems());
            listaTarefas.refresh(); // Atualiza a exibição da lista
        }
    }

    @FXML
    private void abrirAnotacoes()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Anotacoes.fxml"));
            Parent root = loader.load();

            ControladorAnotacoes controlador = loader.getController();
            // Abre janela de anotações
            controlador.configurar(usuarioLogado.getEmail(), gerenciadorDados);
            listaTarefas.refresh();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Anotações de " + usuarioLogado.getNome());
            stage.show();
        } catch(IOException e) {
            mostrarAlerta("Erro", "Não foi possível carregar as anotações");
        }
    }

    @FXML
    private void sair()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
            Parent raiz = loader.load();

            ControladorLogin controladorLogin = loader.getController();

            if(ContextoAplicacao.getGerenciadorUsuarios() != null)
            {
                // Desloga o usuário e volta para tela de login
                ContextoAplicacao.getGerenciadorUsuarios().deslogar();
            }

            Stage palco = (Stage) painelPrincial.getScene().getWindow();
            palco.setScene(new Scene(raiz));
            palco.show();
        } catch(Exception e){
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível sair da aplicação :(");
        }
    }

    private void limparCamposTarefa()
    {
        campoTituloTarefa.clear();
        campoDescricaoTarefa.clear();
        campoDataVencimento.setValue(null);
        dataLembrete.setValue(null);
        horaLembrete.getValueFactory().setValue(LocalTime.of(12, 0));
    }

    private void mostrarAlerta(String titulo, String mensagem)
    {
        // exibe popup
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void abrirConta()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Conta.fxml"));
            Parent root = loader.load();

            ControladorConta controlador = loader.getController();
            controlador.setUsuario(usuarioLogado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciar Conta");
            stage.show();
        } catch(IOException e) {
            mostrarAlerta("Erro", "Não foi possível carregar a tela de gerenciamento de conta.");
        }
    }
}
