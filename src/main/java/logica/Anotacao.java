package logica;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Anotacao
{
    private String titulo;
    private String conteudo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd\'T\'HH:mm:ss.SSS")
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd\'T\'HH:mm:ss.SSS")
    private LocalDateTime dataModificacao;

    public Anotacao(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataCriacao = LocalDateTime.now();
        this.dataModificacao = LocalDateTime.now();
    }

    //construtor
    @JsonCreator
    public Anotacao(@JsonProperty("titulo") String titulo, @JsonProperty("conteudo") String conteudo, @JsonProperty("dataCriacao") LocalDateTime dataCriacao, @JsonProperty("dataModificacao") LocalDateTime dataModificacao)
    {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
        this.dataModificacao = dataModificacao != null ? dataModificacao : LocalDateTime.now();
    }


    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
        this.dataModificacao = LocalDateTime.now();
    }

    public String getConteudo()
    {
        return conteudo;
    }

    public void setConteudo(String conteudo)
    {
        this.conteudo = conteudo;
        this.dataModificacao = LocalDateTime.now();
    }

    public LocalDateTime getDataCriacao()
    {
        return dataCriacao;
    }

    public LocalDateTime getDataModificacao()
    {
        return dataModificacao;
    }

    @Override
    public String toString()
    {
        return titulo + ": " + conteudo;
    }
}
