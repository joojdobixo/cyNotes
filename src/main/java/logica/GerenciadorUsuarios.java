package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GerenciadorUsuarios
{
    private final List<Usuario> usuarios;
    private Usuario usuarioLogado;

    //construtor
    public GerenciadorUsuarios()
    {
        this.usuarios = new ArrayList<>();
        this.usuarioLogado = null;
    }

    //cadastrar novo usuario
    public boolean cadastrarUsuario(String nome, String email, String senha, String telefone)
    {
        // Valida e adiciona novo usuário
        Objects.requireNonNull(nome, "O nome precisa ser preenchido!!");
        Objects.requireNonNull(email, "O email precisa ser preenchido!!");
        Objects.requireNonNull(senha,"A senha precisa ser preenchida!!");

        //verificar se todos os campos foram preenchidos
        if(nome.isBlank() || email.isBlank() || senha.isBlank())
        {
            throw new IllegalArgumentException("Os campos não podem estar em branco!!");
        }

        if(emailJaCadastrado(email))
        {
            return false;
        }

        Usuario novoUsuario = new Usuario(nome, email, senha, telefone);
        return usuarios.add(novoUsuario);
    }

    //verificar se o email já está cadastrado
    private boolean emailJaCadastrado(String email)
    {
        return usuarios.stream().anyMatch(usuario -> usuario.getEmail().equalsIgnoreCase(email));
    }

    public boolean fazerLogin(String email, String senha)
    {
        for(Usuario usuario : usuarios)
        {
            // Verifica se email corresponde (ignorando maiúsculas/minúsculas)
            // e se a senha está correta
            if(usuario.getEmail().equalsIgnoreCase(email) && usuario.verificarSenha(senha))
            {
                usuarioLogado = usuario;
                return true; // Login bem-sucedido
            }
        }
        return false; // Credenciais inválidas
    }

    //excluir conta
    public boolean removerUsuarioLogado()
    {
        // Verifica se há usuário logado e remove da lista
        if(usuarioLogado != null && usuarios.remove(usuarioLogado))
        {
            usuarioLogado = null;// Efetua logout
            return true;
        }
        return false;
    }

    //return Lista imutável de todos os usuários cadastrados
    public Usuario getUsuarioLogado()
    {
        return usuarioLogado;
    }

    public void deslogar()
    {
        usuarioLogado = null;
    }

    public List<Usuario> getUsuarios()
    {
        return Collections.unmodifiableList(usuarios);
    }

    public boolean atualizarUsuario(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            // Busca usuário pelo email (case insensitive)
            if (usuarios.get(i).getEmail().equalsIgnoreCase(usuarioAtualizado.getEmail())) {
                usuarios.set(i, usuarioAtualizado);// Substitui os dados
                return true;
            }
        }
        return false;
    }

    public boolean removerUsuario(String email) {
        // Remove usuário cujo email corresponde
        return usuarios.removeIf(usuario -> usuario.getEmail().equalsIgnoreCase(email));
    }
}