package logica;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Usuario
{
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    //Construtor vazio para desserialização JSON
    public Usuario()
    {

    }
    /**
     * Construtor completo com anotações para serialização JSON
     * @param nome Nome completo do usuário
     * @param email Email único (identificador)
     * @param senha Senha em texto claro (será hasheada posteriormente)
     * @param telefone Telefone opcional (pode ser null)
     */
    @JsonCreator
    public Usuario(@JsonProperty("nome") String nome, @JsonProperty("email") String email, @JsonProperty("senha") String senha, @JsonProperty("telefone") String telefone)
    {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }

    public boolean verificarSenha(String senha)
    {
        return this.senha.equals(senha);
    }
}