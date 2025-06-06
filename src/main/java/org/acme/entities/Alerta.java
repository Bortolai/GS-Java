package org.acme.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Alerta extends _BaseEntity {
    private String titulo;
    private String localizacao;
    private String descricao;
    private String recomendacao;

    public Alerta() {
    }

    public Alerta(int id, boolean deleted, LocalDateTime dataCriacao, String titulo, String localizacao, String descricao, String recomendacao) {
        super(id, deleted, dataCriacao);
        this.titulo = titulo;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.recomendacao = recomendacao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(String recomendacao) {
        this.recomendacao = recomendacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerta alerta = (Alerta) o;
        return Objects.equals(titulo, alerta.titulo) && Objects.equals(localizacao, alerta.localizacao) && Objects.equals(descricao, alerta.descricao) && Objects.equals(recomendacao, alerta.recomendacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, localizacao, descricao, recomendacao);
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "titulo='" + titulo + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", recomendacao='" + recomendacao + '\'' +
                '}';
    }
}
