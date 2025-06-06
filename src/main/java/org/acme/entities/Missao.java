package org.acme.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Missao extends _BaseEntity{
    private String titulo;
    private String localizacao;
    private String organizacao;
    private String descricao;

    public Missao() {
    }

    public Missao(int id, boolean deleted, LocalDateTime dataCriacao, String titulo, String localizacao, String organizacao, String descricao) {
        super(id, deleted, dataCriacao);
        this.titulo = titulo;
        this.localizacao = localizacao;
        this.organizacao = organizacao;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getOrganizacao() {
        return organizacao;
    }

    public void setOrganizacao(String organizacao) {
        this.organizacao = organizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Missao missao = (Missao) o;
        return Objects.equals(getTitulo(), missao.getTitulo()) && Objects.equals(getLocalizacao(), missao.getLocalizacao()) && Objects.equals(getOrganizacao(), missao.getOrganizacao()) && Objects.equals(getDescricao(), missao.getDescricao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitulo(), getLocalizacao(), getOrganizacao(), getDescricao());
    }

    @Override
    public String toString() {
        return "Missao{" +
                "titulo='" + titulo + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", organizacao='" + organizacao + '\'' +
                ", descricao='" + descricao + '\'' +
                "} " + super.toString();
    }
}
