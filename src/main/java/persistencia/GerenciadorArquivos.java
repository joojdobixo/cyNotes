package persistencia;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.nio.file.Files;
import java.time.LocalDate;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import logica.Anotacao;
import logica.Tarefa;
import logica.Usuario;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos implements GerenciadorDados
{
    private static final String DIRETORIO_DADOS = "dados";
    private final ObjectMapper mapper;

    //construtor
    public GerenciadorArquivos()
    {
        this.mapper = new ObjectMapper();
        // Configuração crucial:
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        new File(DIRETORIO_DADOS).mkdirs();
    }

    private String sanitizarEmail(String email)
    {
        return email.replace("@", "_").replace(".", "_");
    }

    private String gerarCaminho(String tipo, String emailUsuario)
    {
        return DIRETORIO_DADOS + File.separator + tipo + "_" + sanitizarEmail(emailUsuario) + ".json";
    }

    @Override
    public void salvarUsuarios(List<Usuario> usuarios)
    {
        try
        {
            mapper.writeValue(new File(DIRETORIO_DADOS + File.separator + "usuarios.json"), usuarios);
        } catch(IOException e){
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> carregarUsuarios()
    {
        File arquivo = new File(DIRETORIO_DADOS + File.separator + "usuarios.json");

        if(!arquivo.exists())
        {
            return new ArrayList<>();
        }

        try
        {
            return mapper.readValue(arquivo, mapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
        } catch(IOException e){
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void salvarTarefas(String emailUsuario, List<Tarefa> tarefas)
    {
        try
        {
            mapper.writeValue(new File(gerarCaminho("tarefas", emailUsuario)), tarefas);
        } catch(IOException e){
            System.err.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    @Override
    public List<Tarefa> carregarTarefas(String emailUsuario)
    {
        File arquivo = new File(gerarCaminho("tarefas", emailUsuario));

        if(!arquivo.exists())
        {
            return new ArrayList<>();
        }

        try
        {
            return mapper.readValue(arquivo, mapper.getTypeFactory().constructCollectionType(List.class, Tarefa.class));
        } catch(IOException e){
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void salvarAnotacoes(String emailUsuario, List<Anotacao> anotacoes)
    {
        try
        {
            mapper.writeValue(new File(gerarCaminho("anotacoes", emailUsuario)), anotacoes);
        } catch(IOException e){
            System.err.println("Erro ao salvar anotações: " + e.getMessage());
        }
    }

    //corrige os segundos em que é criado o arquivo, oq impedia de funcionar
    public void repararArquivoAnotacoes(String emailUsuario)
    {
        File arquivo = new File(gerarCaminho("anotacoes", emailUsuario));
        try
        {
            // 1. Ler o conteúdo atual
            String conteudo = new String(Files.readAllBytes(arquivo.toPath()));

            // 2. Corrigir a formatação das datas
            conteudo = conteudo.replaceAll("\\.\\d+", ".000"); // Remove nanossegundos

            // 3. Reescrever o arquivo
            Files.write(arquivo.toPath(), conteudo.getBytes());

        } catch (IOException e) {
            System.err.println("Falha ao reparar arquivo: " + e.getMessage());
            // Se não conseguir reparar, cria um novo vazio
            try
            {
                mapper.writeValue(arquivo, new ArrayList<Anotacao>());
            } catch (IOException ex) {
                System.err.println("Falha ao criar novo arquivo: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<Anotacao> carregarAnotacoes(String emailUsuario)
    {
        File arquivo = new File(gerarCaminho("anotacoes", emailUsuario));

        if(!arquivo.exists())
        {
            return new ArrayList<>();
        }

        try
        {
            // Tenta ler normalmente primeiro
            return mapper.readValue(arquivo,
                    mapper.getTypeFactory().constructCollectionType(List.class, Anotacao.class));

        } catch (IOException e) {
            System.err.println("Primeira tentativa falhou, tentando reparar...");

            // Se falhar, tenta reparar o arquivo
            repararArquivoAnotacoes(emailUsuario);

            try
            {
                return mapper.readValue(arquivo,
                        mapper.getTypeFactory().constructCollectionType(List.class, Anotacao.class));
            } catch (IOException ex) {
                System.err.println("Falha definitiva ao carregar anotações: " + ex.getMessage());
                return new ArrayList<>();
            }
        }
    }
}