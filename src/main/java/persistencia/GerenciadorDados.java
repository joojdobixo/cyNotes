package persistencia;

import logica.Anotacao;
import logica.Tarefa;
import logica.Usuario;
import java.util.List;

public interface GerenciadorDados
{
    void salvarUsuarios(List<Usuario> usuarios);
    List<Usuario> carregarUsuarios();

    void salvarTarefas(String emailUsuario, List<Tarefa> tarefas);
    List<Tarefa> carregarTarefas(String emailUsuario);

    void salvarAnotacoes(String emailUsuario, List<Anotacao> anotacaos);
    List<Anotacao> carregarAnotacoes(String emailUsuario);
}