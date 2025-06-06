package org.acme.dtos;

import java.time.LocalDate;

public class MissaoRespostaDto {
    private int id;
    private String titulo;
    private String localizacao;
    private String organizacao;
    private String descricao;
    private String dataCriacaoFormata;

    public MissaoRespostaDto() {
    }

    public MissaoRespostaDto(int id, String titulo, String localizacao, String organizacao, String descricao, String dataCriacaoFormata) {
        this.id = id;
        this.titulo = titulo;
        this.localizacao = localizacao;
        this.organizacao = organizacao;
        this.descricao = descricao;
        this.dataCriacaoFormata = dataCriacaoFormata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDataCriacaoFormata() {
        return dataCriacaoFormata;
    }

    public void setDataCriacaoFormata(String dataCriacaoFormata) {
        this.dataCriacaoFormata = dataCriacaoFormata;
    }
}
