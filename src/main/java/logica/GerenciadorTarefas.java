package logica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorTarefas
{
    private List<Tarefa> tarefas;

    //construtor
    public GerenciadorTarefas()
    {
        this.tarefas = new ArrayList<>();
    }

    //adicionar uma tarefa
    public void adicionarTarefa(String titulo, String descricao, LocalDate dataVencimento, LocalDateTime lembrete)
    {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, dataVencimento, lembrete);
        tarefas.add(novaTarefa);
    }

    //editar uma tarefa
    public boolean editarTarefa(int indice, String novoTitulo, String novaDescricao, LocalDateTime novoLembrete)
    {
        if(indice >= 0 && indice < tarefas.size())
        {
            Tarefa tarefa = tarefas.get(indice);
            tarefa.setTitulo(novoTitulo);
            tarefa.setDescricao(novaDescricao);
            tarefa.setLembrete(novoLembrete);
            return true;
        }
        return false;
    }

    //remover uma tarefa
    public boolean removerTarefa(int indice)
    {
        if(indice >= 0 && indice < tarefas.size())
        {
            tarefas.remove(indice);
            return true;
        }
        return false;
    }

    //marcar tarefa como concluída
    public boolean marcarTarefaComoConcluida(int indice)
    {
        if(indice >= 0 && indice < tarefas.size())
        {
            tarefas.get(indice).marcarComoConcluida();
            return true;
        }
        return false;
    }

    //listar todas as tarefas
    public List<Tarefa> getTarefas()
    {
        return new ArrayList<>(tarefas);
    }

    //listar as tarefas com lembrete
    public List<Tarefa> filtrarTarefasComLembrete()
    {
        List<Tarefa> resultado = new ArrayList<>();
        for(Tarefa tarefa : this.tarefas)
        {
            if(tarefa.temLembrete())
            {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }
}