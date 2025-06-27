package logica;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tarefa
{
    private String titulo;
    private String descricao;
    private boolean concluida;
    private LocalDate dataVencimento;
    private LocalDateTime lembrete;
    private LocalDateTime dataCriacao;

    //construtor
    public Tarefa(String titulo, String descricao, LocalDate dataVencimento, LocalDateTime lembrete)
    {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.lembrete = lembrete;
        this.concluida = false;
        this.dataCriacao = LocalDateTime.now();
    }

    public Tarefa(){}

    public void marcarComoConcluida()
    {
        this.concluida = true;
    }

    public void desmarcarComoConcluida()
    {
        this.concluida = false;
    }

    public boolean temLembrete()
    {
        return this.lembrete != null;
    }

    public boolean estaAtrasada()
    {
        return !concluida && dataVencimento != null && LocalDate.now().isAfter(dataVencimento);
    }

    @Override
    public String toString()
    {
        //escreve a frase
        StringBuilder sb = new StringBuilder();
        sb.append(concluida ? "✓ " : "○ ");
        sb.append(titulo);

        if(dataVencimento != null)
        {
            sb.append(" (Vencimento: ").append(dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(")");
        }

        if(estaAtrasada())
        {
            sb.append(" [ATRASADA]"); // Alerta visual para tarefas atrasadas
        }

        return sb.toString();
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public boolean isConcluida()
    {
        return concluida;
    }

    public void setConcluida(boolean concluida)
    {
        this.concluida = concluida;
    }

    public LocalDate getDataVencimento()
    {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento)
    {
        this.dataVencimento = dataVencimento;
    }

    public LocalDateTime getLembrete()
    {
        return lembrete;
    }

    public void setLembrete(LocalDateTime lembrete)
    {
        this.lembrete = lembrete;
    }

    public LocalDateTime getDataCriacao()
    {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao)
    {
        this.dataCriacao = dataCriacao;
    }
}