package org.acme.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario extends _BaseEntity{
    private String nome;
    private String email;
    private String categoria;
    private String telefone;

    public Usuario() {
    }

    public Usuario(int id, boolean deleted, LocalDateTime dataCriacao, String nome, String email, String categoria, String telefone) {
        super(id, deleted, dataCriacao);
        this.nome = nome;
        this.email = email;
        this.categoria = categoria;
        this.telefone = telefone;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getNome(), usuario.getNome()) && Objects.equals(getEmail(), usuario.getEmail()) && Objects.equals(getCategoria(), usuario.getCategoria()) && Objects.equals(getTelefone(), usuario.getTelefone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNome(), getEmail(), getCategoria(), getTelefone());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", categoria='" + categoria + '\'' +
                ", telefone='" + telefone + '\'' +
                "} " + super.toString();
    }
}
