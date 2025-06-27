package logica;

import persistencia.GerenciadorArquivos;
import persistencia.GerenciadorDados;
import java.util.List;

public class ContextoAplicacao
{
    private static final GerenciadorDados gerenciadorDados = new GerenciadorArquivos();
    private static final GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();

    static
    {
        inicializarUsuarios();
    }

    public static void inicializarUsuarios()
    {
        List<Usuario> usuariosCarregados = gerenciadorDados.carregarUsuarios();

        for(Usuario usuario : usuariosCarregados)
        {
            try
            {
                gerenciadorUsuarios.cadastrarUsuario(usuario.getNome(),usuario.getEmail(), usuario.getSenha(), usuario.getTelefone());
            } catch(Exception e){
                System.err.println("Erro ao carregar usuário: " + usuario.getEmail());
            }
        }
    }

    public static GerenciadorDados getGerenciadorDados()
    {
        return gerenciadorDados;
    }

    public static GerenciadorUsuarios getGerenciadorUsuarios()
    {
        return gerenciadorUsuarios;
    }
}