package org.acme.dtos;

public class MissaoCriadaDto {
    private String titulo;
    private String localizacao;
    private String organizacao;
    private String descricao;

    public MissaoCriadaDto() {
    }

    public MissaoCriadaDto(String titulo, String localizacao, String organizacao, String descricao) {
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
}
