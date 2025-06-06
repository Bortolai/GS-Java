package org.acme.dtos;

public class UsuarioCadastroDto {
    private String nome;
    private String email;
    private String categoria;
    private String telefone;
    private String senha;
    private String confirmaSenha;

    public UsuarioCadastroDto() {
    }

    public UsuarioCadastroDto(String nome, String email, String categoria, String telefone, String senha, String confirmaSenha) {
        this.nome = nome;
        this.email = email;
        this.categoria = categoria;
        this.telefone = telefone;
        this.senha = senha;
        this.confirmaSenha = confirmaSenha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }
}
